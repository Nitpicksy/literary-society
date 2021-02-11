package nitpicksy.literarysociety.elasticsearch.repository;

import nitpicksy.literarysociety.elasticsearch.model.BookInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BookInfoRepository extends ElasticsearchRepository<BookInfo, Long> {

}
