package nitpicksy.literarysociety.client;

import nitpicksy.literarysociety.config.FeignClientConfiguration;
import nitpicksy.literarysociety.dto.request.PaymentGatewayPayRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(name = "zuul", configuration = FeignClientConfiguration.class, url = "https://localhost:8080/")
public interface ZuulClient {

    @RequestMapping(method = RequestMethod.GET, path = "payment-gateway/pg-test/health")
    String healthCheck();

    @RequestMapping(method = RequestMethod.POST, path = "payment-gateway/api/orders")
    String pay(@RequestHeader(value = "Auth") String authHeader,
               @RequestBody PaymentGatewayPayRequestDTO paymentGatewayPayRequestDTO);

    @RequestMapping(method = RequestMethod.GET, path = "payment-gateway/api/merchants/{name}/payment-data")
    ResponseEntity<String> getPaymentData(@RequestHeader(value = "Auth") String authHeader, @PathVariable String name);

    @RequestMapping(method = RequestMethod.POST, path = "payment-gateway/api/merchants")
    void addMerchant(@RequestHeader(value = "Auth") String authHeader,
               @RequestBody String merchantName);
}
