package nitpicksy.bank.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl {

    public String healthCheck() {
        StringBuilder sb = new StringBuilder("Bank is up and running!");
        sb.append(System.lineSeparator());

        return sb.toString();
    }
}