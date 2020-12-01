package nitpicksy.literarysociety.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "zuul")
public interface PaymentGatewayClient {

    @RequestMapping(method = RequestMethod.GET, path = "payment-gateway/pg-test/health")
    String healthCheck();

}
