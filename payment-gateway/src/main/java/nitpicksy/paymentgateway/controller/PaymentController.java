package nitpicksy.paymentgateway.controller;

import nitpicksy.paymentgateway.dto.ConfirmPaymentRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(value = "/api/payments", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {

    @PutMapping(value = "/confirm/{merchantOrderId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> confirmPayment(@PathVariable @Positive(message = "Id must be positive.") Long merchantOrderId,
                                      @Valid @RequestBody ConfirmPaymentRequestDTO confirmPaymentRequestDTO) {
        System.out.println("Payment gateway");
        return new ResponseEntity<>( HttpStatus.OK);
    }
}
