package nitpicksy.literarysociety.elastic.repository;

import nitpicksy.literarysociety.elastic.model.GenreIndexingUnit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GenreIndexRepository extends ElasticsearchRepository<GenreIndexingUnit, Long> {

    GenreIndexingUnit findOneById(Long id);

    GenreIndexingUnit findByName(String name);

}
