package nitpicksy.paypalservice.client;

import nitpicksy.paypalservice.dto.response.ConfirmPaymentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "zuul")
public interface ZuulClient {

    @RequestMapping(method = RequestMethod.PUT, path = "/payment-gateway/api/payments/confirm/{merchantOrderId}")
    void confirmPayment(@PathVariable("merchantOrderId") Long merchantOrderId, @RequestBody ConfirmPaymentResponseDTO confirmPaymentResponseDTO);

}