package nitpicksy.literarysociety.elastic.service;

import nitpicksy.literarysociety.elastic.model.BetaReaderIndexingUnit;
import nitpicksy.literarysociety.model.Reader;

import java.util.List;

public interface BetaReaderIndexService {

    void addBetaReader(Reader reader);

    List<BetaReaderIndexingUnit> filterByGenre(String genreName);

    List<BetaReaderIndexingUnit> filterByGenreAndGeolocation(String genreName, Double lat, Double lon);

}
