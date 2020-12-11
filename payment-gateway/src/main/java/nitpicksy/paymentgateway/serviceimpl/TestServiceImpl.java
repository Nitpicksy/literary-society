package nitpicksy.paymentgateway.serviceimpl;

import nitpicksy.paymentgateway.client.PaymentServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl {

    private PaymentServiceClient client;

    @Value("${eureka.instance.instance-id}")
    private String instanceId;

    public String healthCheck() {
        String bankHealthCheck = client.bankHealthCheck();
        String paypalHealthCheck = client.paypalHealthCheck();

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
