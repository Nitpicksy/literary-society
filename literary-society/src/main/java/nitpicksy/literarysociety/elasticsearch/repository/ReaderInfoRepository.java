package nitpicksy.literarysociety.elasticsearch.repository;

import nitpicksy.literarysociety.elasticsearch.model.ReaderInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ReaderInfoRepository extends ElasticsearchRepository<ReaderInfo, Long> {

}

