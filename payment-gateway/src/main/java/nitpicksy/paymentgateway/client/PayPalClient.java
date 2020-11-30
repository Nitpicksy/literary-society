package nitpicksy.paymentgateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "paypal-service")
public interface PayPalClient {

    @RequestMapping(method = RequestMethod.GET, path = "/paypal-test/health")
    String healthCheck();

}
