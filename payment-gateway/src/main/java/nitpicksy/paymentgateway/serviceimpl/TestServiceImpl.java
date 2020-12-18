package nitpicksy.paymentgateway.serviceimpl;

import nitpicksy.paymentgateway.client.ZuulClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class TestServiceImpl {

    private ZuulClient client;

    @Value("${eureka.instance.instance-id}")
    private String instanceId;

    @Value("${API_GATEWAY_URL}")
    private String apiGatewayURL;

    public String healthCheck() {
        String bankPath = apiGatewayURL + "/bank";
        String paypalPath = apiGatewayURL + "/paypal";
        String bitcoin = apiGatewayURL + "/bitcoin";

        String bankHealthCheck = client.healthCheck(URI.create(bankPath));
        String paypalHealthCheck = client.healthCheck(URI.create(paypalPath));
        String bitcoinHealthCheck = client.healthCheck(URI.create(bitcoin));

        StringBuilder sb = new StringBuilder("Payment Gateway ID=");
        sb.append(instanceId);
        sb.append(" is up and running!");
        sb.append(System.lineSeparator());
        sb.append(bankHealthCheck);
        sb.append(paypalHealthCheck);
        sb.append(bitcoinHealthCheck);

        return sb.toString();
    }

    @Autowired
    public TestServiceImpl(ZuulClient client) {
        this.client = client;
    }

}
