package nitpicksy.paymentgateway.serviceimpl;

import nitpicksy.paymentgateway.client.PaymentServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class TestServiceImpl {

    private PaymentServiceClient client;

    @Value("${eureka.instance.instance-id}")
    private String instanceId;

    @Value("${api-gateway.URL}")
    private String apiGatewayURL;

    public String healthCheck() {

        String bankPath = apiGatewayURL + "bank";
        String paypalPath = apiGatewayURL + "paypal";

        String bankHealthCheck = client.healthCheck(URI.create(bankPath));
        String paypalHealthCheck = client.healthCheck(URI.create(paypalPath));

        StringBuilder sb = new StringBuilder("Payment Gateway ID=");
        sb.append(instanceId);
        sb.append(" is up and running!");
        sb.append(System.lineSeparator());
        sb.append(bankHealthCheck);
        sb.append(paypalHealthCheck);

        return sb.toString();
    }

    @Autowired
    public TestServiceImpl(PaymentServiceClient client) {
        this.client = client;
    }

}
