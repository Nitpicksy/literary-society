package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.model.Genre;
import nitpicksy.literarysociety.repository.GenreRepository;
import nitpicksy.literarysociety.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

    private GenreRepository genreRepository;

    @Override
    public Genre findById(Long id) {
        return genreRepository.findOneById(id);
    }

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
