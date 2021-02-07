package nitpicksy.paypalservice.serviceimpl;

import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl {

    public String healthCheck() {
        StringBuilder sb = new StringBuilder("PayPal is up and running!");
        sb.append(System.lineSeparator());

        return sb.toString();
    }

}
