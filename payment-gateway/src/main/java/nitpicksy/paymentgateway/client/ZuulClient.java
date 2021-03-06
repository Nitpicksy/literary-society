package nitpicksy.paymentgateway.client;

import nitpicksy.paymentgateway.config.FeignClientConfiguration;
import nitpicksy.paymentgateway.dto.request.*;
import nitpicksy.paymentgateway.dto.response.LiterarySocietyOrderResponseDTO;
import nitpicksy.paymentgateway.dto.response.PaymentResponseDTO;
import nitpicksy.paymentgateway.dto.response.TransactionResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URI;
import java.util.List;

@FeignClient(name = "zuul", configuration = FeignClientConfiguration.class)
public interface ZuulClient {

    @RequestMapping(method = RequestMethod.GET, path = "/test/health")
    String healthCheck(URI baseUrl);

    /**
     * Generic request that targets payment services in runtime.
     *
     * @param baseUrl: path to API-gateway + path to payment service
     * @param dto
     * @return URLs
     */
    @RequestMapping(method = RequestMethod.POST, path = "/api/payments/pay")
    PaymentResponseDTO forwardPaymentRequest(URI baseUrl, @RequestBody DynamicPaymentDetailsDTO dto);

    /**
     * Generic request that targets literary societies in runtime.
     *
     * @param baseUrl: path to API-gateway + path to payment service
     * @param dto
     */
    @RequestMapping(method = RequestMethod.POST, path = "/api/payments/confirm")
    void confirmPaymentToLiterarySociety(URI baseUrl, @RequestBody LiterarySocietyOrderResponseDTO dto);

    /**
     * Generic request that sends JWT token to added company (literary society).
     *
     * @param baseUrl:  path to API-gateway + path to company
     * @param jwtToken: generated JWT authentication token
     */
    @RequestMapping(method = RequestMethod.POST, path = "/api/auth/accept-pg-token")
    void sendJWTToken(URI baseUrl, @RequestBody JWTRequestDTO jwtRequestDTO);


    @RequestMapping(method = RequestMethod.POST, path = "/api/merchants/{name}/payment-data")
    String supportPaymentMethods(URI baseUrl, @PathVariable String name);

    @RequestMapping(method = RequestMethod.POST, path = "/api/subscriptions/create-plan")
    String createSubscriptionPlan(URI baseUrl, @RequestBody SubscriptionPlanToPaypalDTO planToPaypalDTO);

    @RequestMapping(method = RequestMethod.POST, path = "/api/subscriptions/subscribe")
    String subscribe(URI baseUrl, @RequestBody SubscriptionToPaypalDTO subscriptionToPaypalDTO);

    @RequestMapping(method = RequestMethod.POST, path = "/api/subscriptions/unsubscribe")
    String unsubscribe(URI baseUrl, @RequestBody CancelSubscriptionToPaypalDTO cancelSubscriptionDTO);

    @RequestMapping(method = RequestMethod.GET, path = "/api/merchants/active")
    List<String> getAllMerchants(URI baseUrl);

    @RequestMapping(method = RequestMethod.GET, path = "/api/transactions")
    List<TransactionResponseDTO> getAllTransactions(URI baseUrl);

    @RequestMapping(method = RequestMethod.PUT, path = "/api/payments/choose-payment-methods")
    String choosePaymentMethods(URI baseUrl, @RequestBody Boolean supportPaymentMethods);
}
