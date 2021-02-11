package nitpicksy.literarysociety.elasticsearch.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nitpicksy.literarysociety.elasticsearch.service.GeoPointService;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoPointServiceImpl implements GeoPointService {

    @Override
    public GeoPoint getGeoPoint(String place){
        RestTemplate restTemplate = new RestTemplate();
        String uri = "https://nominatim.openstreetmap.org/search?q=" + place +"&format=json";
        ResponseEntity<String> response;
        try {
            response = restTemplate.getForEntity(uri, String.class);
        } catch (RestClientException e) {
            response = null;
        }
        if (response != null && response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode root = mapper.readTree(response.getBody());
                JsonNode oneNode = root.get(0);
                if(oneNode != null){
                    String lat = oneNode.path("lat").asText();
                    String lon = oneNode.path("lon").asText();
                    if(!lat.isEmpty() && !lon.isEmpty()){
                        return new GeoPoint(Double.parseDouble(lat),Double.parseDouble(lon));
                    }
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
