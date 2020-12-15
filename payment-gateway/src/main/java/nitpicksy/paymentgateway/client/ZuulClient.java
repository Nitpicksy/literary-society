package nitpicksy.paymentgateway.client;

import nitpicksy.paymentgateway.dto.request.DynamicPaymentDetailsDTO;
import nitpicksy.paymentgateway.dto.response.PaymentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URI;

@FeignClient(name = "zuul")
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

}
