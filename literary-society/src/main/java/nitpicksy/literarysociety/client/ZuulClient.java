package nitpicksy.literarysociety.client;

import nitpicksy.literarysociety.dto.request.CancelSubscriptionDTO;
import nitpicksy.literarysociety.dto.request.JWTRequestDTO;
import nitpicksy.literarysociety.dto.request.PaymentGatewayPayRequestDTO;
import nitpicksy.literarysociety.dto.request.SubscriptionDTO;
import nitpicksy.literarysociety.dto.request.SubscriptionPlanDTO;
import nitpicksy.literarysociety.config.FeignClientConfiguration;
import nitpicksy.literarysociety.dto.request.LiterarySocietyOrderRequestDTO;
import nitpicksy.literarysociety.dto.request.PaymentGatewayPayRequestDTO;
import nitpicksy.literarysociety.dto.response.MerchantPaymentGatewayResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@FeignClient(name = "zuul", configuration = FeignClientConfiguration.class, url = "https://localhost:8080/")
public interface ZuulClient {

    @RequestMapping(method = RequestMethod.GET, path = "payment-gateway/pg-test/health")
    String healthCheck();

    @RequestMapping(method = RequestMethod.POST, path = "payment-gateway/api/orders")
    String pay(@RequestHeader(value = "Auth") String authHeader,
               @RequestBody PaymentGatewayPayRequestDTO paymentGatewayPayRequestDTO);

    @RequestMapping(method = RequestMethod.GET, path = "payment-gateway/api/merchants/{name}/payment-data")
    ResponseEntity<String> getPaymentData(@RequestHeader(value = "Auth") String authHeader, @PathVariable String name);

    @RequestMapping(method = RequestMethod.POST, path = "payment-gateway/api/subscriptions/create-plan")
    String createSubscriptionPlan(@RequestHeader(value = "Auth") String authHeader,
                                  @RequestBody SubscriptionPlanDTO subscriptionPlanDTO);

    @RequestMapping(method = RequestMethod.POST, path = "payment-gateway/api/subscriptions/subscribe")
    String subscribe(@RequestHeader(value = "Auth") String authHeader,
                     @RequestBody SubscriptionDTO subscriptionDTO);

    @RequestMapping(method = RequestMethod.POST, path = "payment-gateway/api/subscriptions/unsubscribe")
    String unsubscribe(@RequestHeader(value = "Auth") String authHeader,
                       @RequestBody CancelSubscriptionDTO cancelSubscriptionDTO);

    @RequestMapping(method = RequestMethod.POST, path = "payment-gateway/api/merchants")
    void addMerchant(@RequestHeader(value = "Auth") String authHeader,
               @RequestBody String merchantName);

    @RequestMapping(method = RequestMethod.GET, path = "payment-gateway/api/merchants")
    List<MerchantPaymentGatewayResponseDTO> getAllMerchants(@RequestHeader(value = "Auth") String authHeader);

    @RequestMapping(method = RequestMethod.GET, path = "payment-gateway/api/transactions")
    List<LiterarySocietyOrderRequestDTO> getAllTransactions(@RequestHeader(value = "Auth") String authHeader);

    @RequestMapping(method = RequestMethod.POST, path = "payment-gateway/api/auth/company-refresh")
    JWTRequestDTO refreshAuthenticationToken(@RequestHeader(value = "Auth") String authHeader);
}
