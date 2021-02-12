package nitpicksy.literarysociety.elastic.service;

import nitpicksy.literarysociety.elastic.model.GenreIndexingUnit;
import nitpicksy.literarysociety.model.Genre;

public interface GenreIndexService {

    GenreIndexingUnit addGenre(Genre genre);

    GenreIndexingUnit findById(Long id);

}
