package nitpicksy.literarysociety.client;

import nitpicksy.literarysociety.dto.request.PaymentGatewayPayRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "zuul")
public interface ZuulClient {

    @RequestMapping(method = RequestMethod.GET, path = "payment-gateway/pg-test/health")
    String healthCheck();

    @RequestMapping(method = RequestMethod.POST, path = "payment-gateway/api/orders")
    String pay(@RequestBody PaymentGatewayPayRequestDTO paymentGatewayPayRequestDTO);
}
