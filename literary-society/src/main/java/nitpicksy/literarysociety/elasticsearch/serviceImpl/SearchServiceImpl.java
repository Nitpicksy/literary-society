package nitpicksy.literarysociety.elasticsearch.serviceImpl;

import nitpicksy.literarysociety.elasticsearch.dto.SearchParamDTO;
import nitpicksy.literarysociety.elasticsearch.mapper.BookInfoMapper;
import nitpicksy.literarysociety.elasticsearch.model.BookInfo;
import nitpicksy.literarysociety.elasticsearch.service.SearchService;
import nitpicksy.literarysociety.enumeration.SearchType;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    private final ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Page<BookInfo> combineSearchParams(List<SearchParamDTO> searchParams, int page, int size) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder queryParams = QueryBuilders.boolQuery();

        for (SearchParamDTO searchParam: searchParams) {
            if(searchParam.getAttributeName().equals("genreInfo")){
                buildNestedParam(queryParams,searchParam.getPhraseQuery(), searchParam.getSearchValue(), searchParam.getType());
            }else {
                buildQueryParam(queryParams,searchParam.getPhraseQuery(), searchParam.getAttributeName(), searchParam.getSearchValue(), searchParam.getType());
            }
        }
        return elasticsearchTemplate.queryForPage(createSearchQuery(nativeSearchQueryBuilder, queryParams, page, size), BookInfo.class, new BookInfoMapper());
    }

    @Override
    public Page<BookInfo> search(String searchValue, int page, int size) {

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder queryParams = new BoolQueryBuilder();

        queryParams.should(QueryBuilders.commonTermsQuery("title", searchValue));
        queryParams.should(QueryBuilders.commonTermsQuery("writers", searchValue));
        queryParams.should(QueryBuilders.commonTermsQuery("content", searchValue));

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("genreInfo", boolQuery.should(
                QueryBuilders.commonTermsQuery("genreInfo.name", searchValue)), ScoreMode.Total);

        queryParams.should(nestedQuery);

        return elasticsearchTemplate.queryForPage(createSearchQuery(nativeSearchQueryBuilder, queryParams, page, size), BookInfo.class, new BookInfoMapper());
    }


    private void buildQueryParam(BoolQueryBuilder queryParams, Boolean isPhraseQuery, String attributeName, String searchValue, SearchType type) {
        if(isPhraseQuery){
            if(type.equals(SearchType.AND)){
                queryParams.must(QueryBuilders.matchPhraseQuery(attributeName, searchValue));
            }else if(type.equals(SearchType.OR)){
                queryParams.should(QueryBuilders.matchPhraseQuery(attributeName, searchValue));
            }
        }else {
            if(type.equals(SearchType.AND)){
                queryParams.must(QueryBuilders.commonTermsQuery(attributeName, searchValue));
            }else if(type.equals(SearchType.OR)){
                queryParams.should(QueryBuilders.commonTermsQuery(attributeName, searchValue));
            }
        }

    }

    private void buildNestedParam(BoolQueryBuilder queryParams, Boolean isPhraseQuery, String searchValue, SearchType type) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        NestedQueryBuilder nestedQuery;

        if(isPhraseQuery){
            if(type.equals(SearchType.AND)){
                nestedQuery = QueryBuilders.nestedQuery("genreInfo", boolQuery.must(QueryBuilders.matchPhraseQuery("genreInfo.name", searchValue)), ScoreMode.None);
                queryParams.must(nestedQuery);
            }else if(type.equals(SearchType.OR)){
                nestedQuery = QueryBuilders.nestedQuery("genreInfo", boolQuery.should(QueryBuilders.matchPhraseQuery("genreInfo.name", searchValue)), ScoreMode.None);
                queryParams.should(nestedQuery);
            }
        }else{
            if(type.equals(SearchType.AND)){
                nestedQuery = QueryBuilders.nestedQuery("genreInfo", boolQuery.must(QueryBuilders.commonTermsQuery("genreInfo.name", searchValue)), ScoreMode.None);
                queryParams.must(nestedQuery);
            }else if(type.equals(SearchType.OR)){
                nestedQuery = QueryBuilders.nestedQuery("genreInfo", boolQuery.should(QueryBuilders.commonTermsQuery("genreInfo.name", searchValue)), ScoreMode.None);
                queryParams.should(nestedQuery);
            }
        }
    }
    private SearchQuery createSearchQuery(NativeSearchQueryBuilder searchQueryBuilder, BoolQueryBuilder queryParams, int page, int size){
        return searchQueryBuilder.withQuery(queryParams).withHighlightFields(
                new HighlightBuilder.Field("content")
                        .preTags("<b>")
                        .postTags("</b>")
                        .numOfFragments(1)
                        .fragmentSize(250)
        ).withPageable(PageRequest.of(page, size)).build();
    }

    @Autowired
    public SearchServiceImpl(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }
}
