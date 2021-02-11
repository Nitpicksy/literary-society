package nitpicksy.literarysociety.elasticsearch.service;

import org.springframework.data.elasticsearch.core.geo.GeoPoint;

public interface GeoPointService {

    GeoPoint getGeoPoint(String place);
}
