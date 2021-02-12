package nitpicksy.literarysociety.elasticsearch.serviceImpl;

import nitpicksy.literarysociety.elasticsearch.model.GenreInfo;
import nitpicksy.literarysociety.elasticsearch.repository.GenreInfoRepository;
import nitpicksy.literarysociety.elasticsearch.service.GenreInfoService;
import nitpicksy.literarysociety.model.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreInfoServiceImpl implements GenreInfoService {

    private GenreInfoRepository genreInfoRepository;

    @Override
    public GenreInfo save(Genre genre) {
        return genreInfoRepository.save(new GenreInfo(genre.getId(),genre.getName()));
    }

    @Override
    public List<GenreInfo> findByIds(List<Long> ids) {
        return genreInfoRepository.findByIdIn(ids);
    }

    @Override
    public GenreInfo findById(Long id) {
        return genreInfoRepository.findOneById(id);
    }

    @Autowired
    public GenreInfoServiceImpl(GenreInfoRepository genreInfoRepository) {
        this.genreInfoRepository = genreInfoRepository;
    }
}
