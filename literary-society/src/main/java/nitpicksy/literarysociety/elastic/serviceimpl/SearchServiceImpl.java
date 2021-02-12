package nitpicksy.literarysociety.elastic.serviceimpl;

import nitpicksy.literarysociety.elastic.dto.QueryDTO;
import nitpicksy.literarysociety.elastic.dto.QueryParamDTO;
import nitpicksy.literarysociety.elastic.mapper.BookResultMapper;
import nitpicksy.literarysociety.elastic.model.BookIndexingUnit;
import nitpicksy.literarysociety.elastic.service.SearchService;
import nitpicksy.literarysociety.enumeration.BoolQueryType;
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

@Service
public class SearchServiceImpl implements SearchService {

    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Page<BookIndexingUnit> allFieldsSearch(QueryDTO query) {
        String queryString = query.getSearchAllParam();

        BoolQueryBuilder searchAllQuery = QueryBuilders.boolQuery();
        searchAllQuery.should(QueryBuilders.commonTermsQuery("text", queryString));
        searchAllQuery.should(QueryBuilders.commonTermsQuery("title", queryString));
        searchAllQuery.should(QueryBuilders.commonTermsQuery("writers", queryString));

        BoolQueryBuilder searchGenreQuery = QueryBuilders.boolQuery();
        searchGenreQuery.should(QueryBuilders.commonTermsQuery("genre.name", queryString));
        NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("genre", searchGenreQuery, ScoreMode.Total);
        searchAllQuery.should(nestedQuery);

        SearchQuery searchQuery = includeHighlightConfig(searchAllQuery, query.getPageNum(), query.getPageSize());
        return elasticsearchTemplate.queryForPage(searchQuery, BookIndexingUnit.class, new BookResultMapper());
    }

    @Override
    public Page<BookIndexingUnit> combinedSearch(QueryDTO query) {

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        for (QueryParamDTO queryParam : query.getQueryParams()) {
            String name = queryParam.getName();
            String value = queryParam.getValue();

            if (name.equals("genre")) {
                buildNestedParam(boolQuery, queryParam.getIsPhrase(), value, queryParam.getBoolQueryType());
            } else {
                buildQueryParam(boolQuery, queryParam.getIsPhrase(), name, value, queryParam.getBoolQueryType());
            }
        }

        SearchQuery searchQuery = includeHighlightConfig(boolQuery, query.getPageNum(), query.getPageSize());
        return elasticsearchTemplate.queryForPage(searchQuery, BookIndexingUnit.class, new BookResultMapper());
    }

    private SearchQuery includeHighlightConfig(BoolQueryBuilder paramsQuery, int pageNum, int pageSize) {
        HighlightBuilder.Field highlight = new HighlightBuilder.Field("text")
                .preTags("<b>")
                .postTags("</b>")
                .numOfFragments(1)
                .fragmentSize(250);

        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();
        return searchQuery
                .withQuery(paramsQuery)
                .withHighlightFields(highlight)
                .withPageable(PageRequest.of(pageNum - 1, pageSize)).build();
    }

    private void buildQueryParam(BoolQueryBuilder searchQuery, boolean isPhrase, String name, String value, BoolQueryType boolQueryType) {
        if (boolQueryType == BoolQueryType.AND && isPhrase) {
            searchQuery.must(QueryBuilders.matchPhraseQuery(name, value));
        } else if (boolQueryType == BoolQueryType.AND) {
            searchQuery.must(QueryBuilders.commonTermsQuery(name, value));
        } else if (boolQueryType == BoolQueryType.OR && isPhrase) {
            searchQuery.should(QueryBuilders.matchPhraseQuery(name, value));
        } else if (boolQueryType == BoolQueryType.OR) {
            searchQuery.should(QueryBuilders.commonTermsQuery(name, value));
        }
    }

    private void buildNestedParam(BoolQueryBuilder searchQuery, boolean isPhrase, String value, BoolQueryType boolQueryType) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        NestedQueryBuilder nestedQuery;

        if (boolQueryType == BoolQueryType.AND && isPhrase) {
            nestedQuery = QueryBuilders.nestedQuery("genre", boolQuery.must(QueryBuilders.matchPhraseQuery("genre.name", value)), ScoreMode.None);
            searchQuery.must(nestedQuery);
        } else if (boolQueryType == BoolQueryType.AND) {
            nestedQuery = QueryBuilders.nestedQuery("genre", boolQuery.must(QueryBuilders.commonTermsQuery("genre.name", value)), ScoreMode.None);
            searchQuery.must(nestedQuery);
        } else if (boolQueryType == BoolQueryType.OR && isPhrase) {
            nestedQuery = QueryBuilders.nestedQuery("genre", boolQuery.should(QueryBuilders.matchPhraseQuery("genre.name", value)), ScoreMode.None);
            searchQuery.should(nestedQuery);
        } else if (boolQueryType == BoolQueryType.OR) {
            nestedQuery = QueryBuilders.nestedQuery("genre", boolQuery.should(QueryBuilders.commonTermsQuery("genre.name", value)), ScoreMode.None);
            searchQuery.should(nestedQuery);
        }
    }

    @Autowired
    public SearchServiceImpl(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }
}
