package nitpicksy.literarysociety.elasticsearch.service;

import nitpicksy.literarysociety.elasticsearch.model.ReaderInfo;
import nitpicksy.literarysociety.model.Reader;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

public interface ReaderInfoService {

    ReaderInfo save(Reader reader);
}
