package nitpicksy.literarysociety.elastic.serviceimpl;

import nitpicksy.literarysociety.elastic.model.GenreIndexingUnit;
import nitpicksy.literarysociety.elastic.repository.GenreIndexRepository;
import nitpicksy.literarysociety.elastic.service.GenreIndexService;
import nitpicksy.literarysociety.model.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
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

    @Override
    public List<GenreIndexingUnit> findByIds(List<Long> ids) {
        return genreIndexRepository.findByIdIn(ids);
    }

    @Autowired
    public GenreIndexServiceImpl(ElasticsearchTemplate elasticsearchTemplate, GenreIndexRepository genreIndexRepository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.genreIndexRepository = genreIndexRepository;
    }

}
