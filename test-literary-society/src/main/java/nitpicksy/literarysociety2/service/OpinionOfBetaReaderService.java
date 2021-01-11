package nitpicksy.literarysociety2.service;

import nitpicksy.literarysociety2.model.OpinionOfBetaReader;

import java.util.List;

public interface OpinionOfBetaReaderService {

    List<OpinionOfBetaReader> findByBookId(Long bookId);

}
