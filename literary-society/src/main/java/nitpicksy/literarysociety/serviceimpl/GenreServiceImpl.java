package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.elasticsearch.repository.GenreInfoRepository;
import nitpicksy.literarysociety.elasticsearch.service.GenreInfoService;
import nitpicksy.literarysociety.model.Genre;
import nitpicksy.literarysociety.repository.GenreRepository;
import nitpicksy.literarysociety.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

    private GenreInfoService genreInfoService;

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

    @Override
    public Genre save(Genre genre) {
        Genre savedGenre = genreRepository.saveAndFlush(genre);
        System.out.println(savedGenre.getId());
        genreInfoService.save(savedGenre);
        return savedGenre;
    }

    @Autowired
    public GenreServiceImpl(GenreInfoService genreInfoService, GenreRepository genreRepository) {
        this.genreInfoService = genreInfoService;
        this.genreRepository = genreRepository;
    }
}
