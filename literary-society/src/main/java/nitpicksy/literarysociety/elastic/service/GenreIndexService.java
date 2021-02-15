package nitpicksy.literarysociety.elastic.service;

import nitpicksy.literarysociety.elastic.model.GenreIndexingUnit;
import nitpicksy.literarysociety.model.Genre;

import java.util.List;

public interface GenreIndexService {

    GenreIndexingUnit addGenre(Genre genre);

    GenreIndexingUnit findById(Long id);

    List<GenreIndexingUnit> findByIds(List<Long> ids);

}
