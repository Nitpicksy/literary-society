package nitpicksy.literarysociety.elasticsearch.repository;

import nitpicksy.literarysociety.elasticsearch.model.GenreInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Collection;
import java.util.List;

public interface GenreInfoRepository extends ElasticsearchRepository<GenreInfo, Long> {

    List<GenreInfo> findByIdIn(Collection<Long> ids);

    GenreInfo findOneById(Long id);
}
