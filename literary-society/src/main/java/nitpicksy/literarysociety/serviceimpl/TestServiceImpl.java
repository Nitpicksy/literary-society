package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.client.ZuulClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl {

    private ZuulClient zuulClient;


    public String healthCheck() {
        System.out.println("Hello from health LS!");
        String response = zuulClient.healthCheck();
        StringBuilder sb = new StringBuilder("Literary Society is up and running!" + response.toString());
        sb.append(System.lineSeparator());
        sb.append(response);

        return sb.toString();
    }

    @Autowired
    public TestServiceImpl(ZuulClient zuulClient) {
        this.zuulClient = zuulClient;
    }
}