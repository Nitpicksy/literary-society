package nitpicksy.literarysociety.elastic.repository;

import nitpicksy.literarysociety.elastic.model.GenreIndexingUnit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface GenreIndexRepository extends ElasticsearchRepository<GenreIndexingUnit, Long> {

    GenreIndexingUnit findOneById(Long id);

    List<GenreIndexingUnit> findByIdIn(List<Long> ids);

    GenreIndexingUnit findByName(String name);
}
