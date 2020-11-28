package nitpicksy.paymentgateway.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${eureka.instance.instance-id}")
    private String instanceId;

    public String healthCheck() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        String response = restTemplate.exchange("http://localhost:8077/paypal-test/health",
                HttpMethod.GET, httpEntity, String.class).getBody();

        StringBuilder sb = new StringBuilder("Payment Gateway ID=");
        sb.append(instanceId);
        sb.append(" is up and running!");
        sb.append(System.lineSeparator());
        sb.append(response);

        return sb.toString();
    }

    @Autowired
    public TestServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
