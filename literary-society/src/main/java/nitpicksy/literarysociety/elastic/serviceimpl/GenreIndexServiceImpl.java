package nitpicksy.literarysociety.elastic.serviceimpl;

import nitpicksy.literarysociety.elastic.model.BetaReaderIndexingUnit;
import nitpicksy.literarysociety.elastic.model.GenreIndexingUnit;
import nitpicksy.literarysociety.elastic.repository.GenreIndexRepository;
import nitpicksy.literarysociety.elastic.service.BetaReaderIndexService;
import nitpicksy.literarysociety.elastic.service.GenreIndexService;
import nitpicksy.literarysociety.model.Genre;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreIndexServiceImpl implements GenreIndexService {

    private ElasticsearchTemplate elasticsearchTemplate;

    private GenreIndexRepository genreIndexRepository;

    @Override
    public GenreIndexingUnit addGenre(Genre genre) {
        return genreIndexRepository.save(new GenreIndexingUnit(genre.getId(), genre.getName()));
    }

    @Override
    public GenreIndexingUnit findById(Long id) {
        return genreIndexRepository.findOneById(id);
    }

    @Autowired
    public GenreIndexServiceImpl(ElasticsearchTemplate elasticsearchTemplate, GenreIndexRepository genreIndexRepository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.genreIndexRepository = genreIndexRepository;
    }

}
