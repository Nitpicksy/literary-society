package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.client.PaymentGatewayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl {

    private PaymentGatewayClient paymentGatewayClient;

    public String healthCheck() {
        String response = paymentGatewayClient.healthCheck();
        System.out.println("Hello from health LS!");

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