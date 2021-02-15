package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.model.Reader;

import java.util.List;

public interface ReaderService {

    List<Reader> findByIds(List<Long> ids);

    Reader save(Reader reader);

    List<Reader> findBetaReaders(Long genreId);
}
