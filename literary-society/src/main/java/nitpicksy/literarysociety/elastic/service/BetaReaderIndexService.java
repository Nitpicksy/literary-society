package nitpicksy.literarysociety.elastic.service;

import nitpicksy.literarysociety.elastic.model.BetaReaderIndexingUnit;
import nitpicksy.literarysociety.model.Reader;

import java.util.List;

public interface BetaReaderIndexService {

    BetaReaderIndexingUnit addBetaReader(Reader reader);

    List<BetaReaderIndexingUnit> filterByGeoLocation(Double lat, Double lon);

    List<BetaReaderIndexingUnit> filterByGenreAndGeoLocation(String genreName, Double lat, Double lon);

}
