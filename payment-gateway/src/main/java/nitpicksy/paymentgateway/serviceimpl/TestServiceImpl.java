package nitpicksy.paymentgateway.serviceimpl;

import nitpicksy.paymentgateway.client.PayPalClient;
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

    private PayPalClient payPalClient;

    @Value("${eureka.instance.instance-id}")
    private String instanceId;

    public String healthCheck() {
        String response = payPalClient.healthCheck();

        StringBuilder sb = new StringBuilder("Payment Gateway ID=");
        sb.append(instanceId);
        sb.append(" is up and running!");
        sb.append(System.lineSeparator());
        sb.append(response);

        return sb.toString();
    }

    @Autowired
    public TestServiceImpl(PayPalClient payPalClient) {
        this.payPalClient = payPalClient;
    }

}
