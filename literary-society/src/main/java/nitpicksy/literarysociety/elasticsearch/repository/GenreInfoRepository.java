package nitpicksy.literarysociety.elasticsearch.repository;

import nitpicksy.literarysociety.elasticsearch.model.GenreInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GenreInfoRepository extends ElasticsearchRepository<GenreInfo, Long> {

}
