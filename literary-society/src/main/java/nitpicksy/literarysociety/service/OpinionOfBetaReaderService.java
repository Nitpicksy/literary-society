package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.model.OpinionOfBetaReader;

import java.util.List;

public interface OpinionOfBetaReaderService {

    List<OpinionOfBetaReader> findByBookId(Long bookId);

}
