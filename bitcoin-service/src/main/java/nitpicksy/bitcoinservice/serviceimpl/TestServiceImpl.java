package nitpicksy.bitcoinservice.serviceimpl;

import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl {

    public String healthCheck() {
        StringBuilder sb = new StringBuilder("Bitcoin is up and running!");
        sb.append(System.lineSeparator());

        return sb.toString();
    }
}