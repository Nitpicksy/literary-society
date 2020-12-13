package nitpicksy.paymentgateway.controller;

import nitpicksy.paymentgateway.dto.request.ConfirmPaymentRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping(value = "/api/payments", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {

    @PutMapping(value = "/confirm/{merchantOrderId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> confirmPayment(@PathVariable @Positive(message = "Id must be positive.") Long merchantOrderId,
                                               @Valid @RequestBody ConfirmPaymentRequestDTO confirmPaymentRequestDTO) {
        System.out.println("Payment gateway");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
