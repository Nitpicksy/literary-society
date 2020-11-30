package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.client.PaymentGatewayClient;
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

    private PaymentGatewayClient paymentGatewayClient;

    public String healthCheck() {
        String response = paymentGatewayClient.healthCheck();

        StringBuilder sb = new StringBuilder("Literary Society is up and running!");
        sb.append(System.lineSeparator());
        sb.append(response);

        return sb.toString();
    }

    @Autowired
    public TestServiceImpl(PaymentGatewayClient paymentGatewayClient) {
        this.paymentGatewayClient = paymentGatewayClient;
    }
}
