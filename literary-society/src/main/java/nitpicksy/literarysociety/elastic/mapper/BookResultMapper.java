package nitpicksy.literarysociety.elastic.mapper;

import com.google.gson.Gson;
import lombok.NoArgsConstructor;
import nitpicksy.literarysociety.elastic.model.BookIndexingUnit;
import nitpicksy.literarysociety.elastic.model.GenreIndexingUnit;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class BookResultMapper implements SearchResultMapper {

    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {

        List<BookIndexingUnit> results = new ArrayList<>();
        for (SearchHit searchHit : searchResponse.getHits()) {
            if (searchResponse.getHits().getHits().length <= 0) {
                return null;
            }

            Map<String, Object> source = searchHit.getSourceAsMap();
            Gson gson = new Gson();

            BookIndexingUnit bookIdxUnit = new BookIndexingUnit();
            bookIdxUnit.setId(Long.valueOf(searchHit.getId()));
            bookIdxUnit.setTitle((String) source.get("title"));
            bookIdxUnit.setWriters((String) source.get("writers"));
            bookIdxUnit.setGenre(gson.fromJson(source.get("genre").toString(), GenreIndexingUnit.class));
            bookIdxUnit.setOpenAccess((boolean) source.get("openAccess"));

            String highlight = "";
            try {
                highlight = "..." + searchHit.getHighlightFields().get("text").fragments()[0].toString() + "...";
            } catch (Exception e) {
                highlight = "";
            }
            bookIdxUnit.setText(highlight);

            results.add(bookIdxUnit);
        }

        if (!results.isEmpty()) {
            return new AggregatedPageImpl(results);
        }

        return null;
    }

}
