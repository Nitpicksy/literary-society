package nitpicksy.literarysociety.elasticsearch.mapper;

import com.google.gson.Gson;
import nitpicksy.literarysociety.elasticsearch.model.BookInfo;
import nitpicksy.literarysociety.elasticsearch.model.GenreInfo;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookInfoMapper implements SearchResultMapper {

    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
        List<BookInfo> result = new ArrayList<>();

        for (SearchHit searchHit : searchResponse.getHits()) {
            if (searchResponse.getHits().getHits().length <= 0) {
                return null;
            }

            Gson gson=new Gson();
            Map<String, Object> source = searchHit.getSourceAsMap();

            BookInfo bookInfo = new BookInfo(Long.valueOf(searchHit.getId()),
                    (String) source.get("title"), (String) source.get("writers"), (boolean) source.get("openAccess"),
                    gson.fromJson(source.get("genreInfo").toString(), GenreInfo.class));

            String highValue;
            try {
                highValue = "..."+ searchHit.getHighlightFields().get("content").fragments()[0].toString() +"...";
            }catch(Exception e) {
                highValue = "";
            }

            bookInfo.setContent(highValue);

            result.add(bookInfo);
        }
        if (result.size() > 0) {
            return new AggregatedPageImpl(result);
        }

        return null;
    }
}
