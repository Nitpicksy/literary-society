package nitpicksy.literarysociety.elastic.serviceimpl;

import nitpicksy.literarysociety.elastic.dto.QueryDTO;
import nitpicksy.literarysociety.elastic.dto.QueryParamDTO;
import nitpicksy.literarysociety.elastic.mapper.BookResultMapper;
import nitpicksy.literarysociety.elastic.model.BookIndexingUnit;
import nitpicksy.literarysociety.elastic.model.GenreIndexingUnit;
import nitpicksy.literarysociety.elastic.repository.BookIndexRepository;
import nitpicksy.literarysociety.elastic.service.BookIndexService;
import nitpicksy.literarysociety.elastic.service.GenreIndexService;
import nitpicksy.literarysociety.enumeration.BoolQueryType;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.PDFDocument;
import nitpicksy.literarysociety.service.PDFDocumentService;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BookIndexServiceImpl implements BookIndexService {

    private ElasticsearchTemplate elasticsearchTemplate;

    private BookIndexRepository bookIndexRepository;

    private GenreIndexService genreIndexService;

    private PDFDocumentService pdfDocumentService;

    @Async("processExecutor")
    @Override
    public void addBook(Book book) {
        BookIndexingUnit bookIdxUnit = new BookIndexingUnit();
        bookIdxUnit.setId(book.getId());
        bookIdxUnit.setTitle(book.getTitle());
        bookIdxUnit.setWriters(book.getWritersNames());

        GenreIndexingUnit genreIdxUnit = genreIndexService.findById(book.getGenre().getId());
        bookIdxUnit.setGenre(genreIdxUnit);

        boolean openAccess = book.getPublishingInfo().getPrice() <= 0;
        bookIdxUnit.setOpenAccess(openAccess);

        String text;
        PDFDocument bookPDFDoc = pdfDocumentService.findByBookId(book.getId());
        try {
            text = pdfDocumentService.extractText(bookPDFDoc);
        } catch (IOException e) {
            text = "";
        }
        bookIdxUnit.setText(text);

        bookIndexRepository.save(bookIdxUnit);
    }

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
                includeNestedParam(boolQuery, queryParam.getIsPhrase(), value, queryParam.getBoolQueryType());
            } else {
                includeQueryParam(boolQuery, queryParam.getIsPhrase(), name, value, queryParam.getBoolQueryType());
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
                .withPageable(PageRequest.of(pageNum - 1, pageSize))
                .build();
    }

    private void includeQueryParam(BoolQueryBuilder searchQuery, boolean isPhrase, String name, String value, BoolQueryType boolQueryType) {
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

    private void includeNestedParam(BoolQueryBuilder searchQuery, boolean isPhrase, String value, BoolQueryType boolQueryType) {
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
    public BookIndexServiceImpl(ElasticsearchTemplate elasticsearchTemplate, BookIndexRepository bookIndexRepository,
                                GenreIndexService genreIndexService, PDFDocumentService pdfDocumentService) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.bookIndexRepository = bookIndexRepository;
        this.genreIndexService = genreIndexService;
        this.pdfDocumentService = pdfDocumentService;
    }
}
