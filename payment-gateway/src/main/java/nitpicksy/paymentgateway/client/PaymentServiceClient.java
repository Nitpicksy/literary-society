package nitpicksy.paymentgateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URI;

@FeignClient(name = "zuul")
public interface PaymentServiceClient {

    @RequestMapping(method = RequestMethod.GET, path = "/test/health")
    String healthCheck(URI baseUrl);

}
