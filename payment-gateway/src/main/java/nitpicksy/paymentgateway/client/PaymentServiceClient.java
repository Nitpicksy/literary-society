package nitpicksy.paymentgateway.client;

import nitpicksy.paymentgateway.dto.request.DynamicPaymentDetailsDTO;
import nitpicksy.paymentgateway.dto.response.PaymentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URI;

@FeignClient(name = "zuul")
public interface PaymentServiceClient {

    @RequestMapping(method = RequestMethod.GET, path = "/test/health")
    String healthCheck(URI baseUrl);

    @RequestMapping(method = RequestMethod.POST, path = "/api/payments/pay")
    PaymentResponseDTO forwardPaymentRequest(URI baseUrl, @RequestBody DynamicPaymentDetailsDTO dto);

}
