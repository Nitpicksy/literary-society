package nitpicksy.paypalservice.controller;

import com.paypal.base.rest.PayPalRESTException;
import nitpicksy.paypalservice.dto.request.PaymentRequestDTO;
import nitpicksy.paypalservice.dto.response.PaymentResponseDTO;
import nitpicksy.paypalservice.mapper.PaymentRequestMapper;
import nitpicksy.paypalservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@Validated
@RestController
@RequestMapping(value = "/api/payments", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {

    private PaymentService paymentService;

    private PaymentRequestMapper paymentRequestMapper;

    @PostMapping(value = "/pay", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentResponseDTO> pay(@Valid @RequestBody PaymentRequestDTO paymentRequestDTO) throws PayPalRESTException {
        return new ResponseEntity<>(paymentService.createPayment(paymentRequestMapper.toEntity(paymentRequestDTO)), HttpStatus.OK);
    }

    @GetMapping(value = "/confirm")
    public ResponseEntity<Void> confirmPayment(@RequestParam("paymentId") String paymentId,
                                               @RequestParam("PayerID") String payerId) throws PayPalRESTException {
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create(paymentService.executePayment(paymentId, payerId)))
                .build();
    }

    @Autowired
    public PaymentController(PaymentService paymentService, PaymentRequestMapper paymentRequestMapper) {
        this.paymentService = paymentService;
        this.paymentRequestMapper = paymentRequestMapper;
    }
}
