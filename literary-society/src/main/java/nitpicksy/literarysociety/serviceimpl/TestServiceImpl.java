package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.client.PaymentGatewayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TestServiceImpl {

    private PaymentGatewayClient paymentGatewayClient;

    @Autowired
    private RestTemplate restTemplate;

    public String healthCheck() {
        System.out.println("Hello from health LS!");
        String response = paymentGatewayClient.healthCheck();
//        ResponseEntity<String> response = restTemplate.exchange("https://localhost:8080/payment-gateway/pg-test/health", HttpMethod.GET, new HttpEntity<>(null), String.class);
        StringBuilder sb = new StringBuilder("Literary Society is up and running!" + response.toString());
        sb.append(System.lineSeparator());
        sb.append(response);

        return sb.toString();
    }

    @Autowired
    public TestServiceImpl(PaymentGatewayClient paymentGatewayClient) {
        this.paymentGatewayClient = paymentGatewayClient;
    }
}