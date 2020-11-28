package nitpicksy.literarysociety.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class TestServiceImpl {

    private RestTemplate restTemplate;

    public String healthCheck() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        String response = restTemplate.exchange("http://payment-gateway/pg-test/health",
                HttpMethod.GET, httpEntity, String.class).getBody();

        StringBuilder sb = new StringBuilder("Literary Society is up and running!");
        sb.append(System.lineSeparator());
        sb.append(response);

        return sb.toString();
    }

    @Autowired
    public TestServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
