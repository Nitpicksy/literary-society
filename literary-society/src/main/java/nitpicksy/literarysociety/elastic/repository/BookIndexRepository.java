package nitpicksy.literarysociety.elastic.repository;

import nitpicksy.literarysociety.elastic.model.BookIndexingUnit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BookIndexRepository extends ElasticsearchRepository<BookIndexingUnit, Long> {

    List<BookIndexingUnit> findByWriters(String writers);

    List<BookIndexingUnit> findAll();
}