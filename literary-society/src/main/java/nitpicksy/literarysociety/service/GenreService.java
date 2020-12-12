package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.model.Genre;

import java.util.List;

public interface GenreService {

    List<Genre> findAll();

    List<Genre> findWithIds(List<Long> ids);

}
