package nitpicksy.bank.client;

import nitpicksy.bank.config.FeignClientConfiguration;
import nitpicksy.bank.dto.request.PCCRequestDTO;
import nitpicksy.bank.dto.response.ConfirmPaymentResponseDTO;
import nitpicksy.bank.dto.response.PCCResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "zuul",configuration = FeignClientConfiguration.class, url = "https://localhost:8080/")
public interface ZuulClient {

    @RequestMapping(method = RequestMethod.PUT, path = "/payment-gateway/api/payments/confirm/{merchantOrderId}")
    void confirmPayment(@PathVariable("merchantOrderId") String merchantOrderId, @RequestBody ConfirmPaymentResponseDTO confirmPaymentResponseDTO);

    @RequestMapping(method = RequestMethod.POST, path = "/pcc/api/payments/pay/")
    PCCResponseDTO pay(@RequestBody PCCRequestDTO pccRequestDTO);
}
