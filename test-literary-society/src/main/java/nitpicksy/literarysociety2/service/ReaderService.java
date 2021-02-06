package nitpicksy.literarysociety2.service;

import nitpicksy.literarysociety2.model.Reader;

import java.util.List;

public interface ReaderService {

    List<Reader> findByIds(List<Long> ids);

    Reader save(Reader reader);
}
