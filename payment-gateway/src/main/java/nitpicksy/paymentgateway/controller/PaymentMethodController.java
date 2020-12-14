package nitpicksy.paymentgateway.controller;

import nitpicksy.paymentgateway.dto.response.PaymentMethodResponseDTO;
import nitpicksy.paymentgateway.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping(value = "/api/payment-methods", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentMethodController {

    private PaymentMethodService paymentMethodService;

    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentMethodResponseDTO> getPaymentMethodsForMerchant(@PathVariable("orderId") @Positive(message = "Id must be positive.") Long orderId) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Autowired
    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }
}
