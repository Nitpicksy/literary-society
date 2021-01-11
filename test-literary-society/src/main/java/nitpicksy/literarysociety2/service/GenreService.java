package nitpicksy.literarysociety2.service;

import nitpicksy.literarysociety2.model.Genre;

import java.util.List;

public interface GenreService {

    Genre findById(Long id);

    List<Genre> findAll();

    List<Genre> findWithIds(List<Long> ids);

}
