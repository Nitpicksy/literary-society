package nitpicksy.literarysociety.elasticsearch.service;

import nitpicksy.literarysociety.elasticsearch.model.GenreInfo;
import nitpicksy.literarysociety.model.Genre;

import java.util.List;

public interface GenreInfoService {

    GenreInfo save(Genre genre);

    List<GenreInfo> findByIds(List<Long> ids);

    GenreInfo findById(Long id);
}
