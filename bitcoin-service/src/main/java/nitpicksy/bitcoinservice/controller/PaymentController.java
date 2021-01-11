package nitpicksy.bitcoinservice.controller;

import nitpicksy.bitcoinservice.dto.request.PaymentCallbackDTO;
import nitpicksy.bitcoinservice.dto.request.PaymentRequestDTO;
import nitpicksy.bitcoinservice.dto.response.PaymentResponseDTO;
import nitpicksy.bitcoinservice.mapper.PaymentMapper;
import nitpicksy.bitcoinservice.service.PaymentService;
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
    private PaymentMapper paymentRequestMapper;

    @PostMapping(value = "/pay", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentResponseDTO> pay(@Valid @RequestBody PaymentRequestDTO paymentRequestDTO) {
        return new ResponseEntity<>(paymentService.createPayment(paymentRequestMapper.toEntity(paymentRequestDTO)), HttpStatus.OK);
    }

    @RequestMapping(value = "/callback", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> callback(@ModelAttribute PaymentCallbackDTO callbackDTO) {
        paymentService.executePayment(callbackDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Autowired
    public PaymentController(PaymentService paymentService, PaymentMapper paymentRequestMapper) {
        this.paymentService = paymentService;
        this.paymentRequestMapper = paymentRequestMapper;
    }
}
