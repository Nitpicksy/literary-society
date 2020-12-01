package nitpicksy.paymentgateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "zuul")
public interface PayPalClient {

    @RequestMapping(method = RequestMethod.GET, path = "paypal-service/paypal-test/health")
    String healthCheck();

}
