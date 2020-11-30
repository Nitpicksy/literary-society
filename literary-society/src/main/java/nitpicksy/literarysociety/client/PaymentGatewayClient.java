package nitpicksy.literarysociety.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "payment-gateway")
public interface PaymentGatewayClient {

    @RequestMapping(method = RequestMethod.GET, path = "/pg-test/health")
    String healthCheck();

}
