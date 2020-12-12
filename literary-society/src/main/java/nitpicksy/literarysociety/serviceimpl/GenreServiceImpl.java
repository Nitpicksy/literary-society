package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.model.Genre;
import nitpicksy.literarysociety.repository.GenreRepository;
import nitpicksy.literarysociety.service.GenreService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

    private GenreRepository genreRepository;

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public List<Genre> findWithIds(List<Long> ids) {
        return genreRepository.findByIdIn(ids);
    }

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

}
