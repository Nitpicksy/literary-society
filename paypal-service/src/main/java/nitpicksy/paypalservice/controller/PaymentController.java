package nitpicksy.paypalservice.controller;

import com.paypal.base.rest.PayPalRESTException;
import nitpicksy.paypalservice.dto.request.ConfirmPaymentDTO;
import nitpicksy.paypalservice.dto.request.PaymentRequestDTO;
import nitpicksy.paypalservice.dto.response.PaymentResponseDTO;
import nitpicksy.paypalservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping(value = "/api/payments", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {

    private PaymentService paymentService;

    @PostMapping(value = "/pay", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentResponseDTO> pay(@Valid @RequestBody PaymentRequestDTO paymentRequestDTO) throws PayPalRESTException {
        return new ResponseEntity<>(paymentService.createPayment(paymentRequestDTO), HttpStatus.OK);
    }

    @PostMapping(value = "/confirm/{paymentId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> confirmPayment(@PathVariable String paymentId,
                                                 @Valid @RequestBody ConfirmPaymentDTO confirmPaymentDTO) throws PayPalRESTException {
        return new ResponseEntity<>(paymentService.completePayment(paymentId, confirmPaymentDTO), HttpStatus.OK);
    }

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
