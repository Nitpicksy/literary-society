package nitpicksy.paymentgateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "zuul")
public interface PaymentServiceClient {

    @RequestMapping(method = RequestMethod.GET, path = "bank/test/health")
    String bankHealthCheck();

    @RequestMapping(method = RequestMethod.GET, path = "paypal/test/health")
    String paypalHealthCheck();
}
