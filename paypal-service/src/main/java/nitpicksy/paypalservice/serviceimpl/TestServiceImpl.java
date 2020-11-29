package nitpicksy.paypalservice.serviceimpl;

import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl {

    public String healthCheck() {
        return "PayPal Service is up an running!";
    }

}
