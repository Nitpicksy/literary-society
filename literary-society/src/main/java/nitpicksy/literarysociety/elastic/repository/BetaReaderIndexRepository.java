package nitpicksy.literarysociety.elastic.repository;

import nitpicksy.literarysociety.elastic.model.BetaReaderIndexingUnit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BetaReaderIndexRepository extends ElasticsearchRepository<BetaReaderIndexingUnit, Long> {
}
