package nitpicksy.bank.controller;

import nitpicksy.bank.dto.request.ConfirmPaymentDTO;
import nitpicksy.bank.dto.request.PaymentRequestDTO;
import nitpicksy.bank.dto.response.PaymentResponseDTO;
import nitpicksy.bank.mapper.PaymentRequestMapper;
import nitpicksy.bank.model.Merchant;
import nitpicksy.bank.service.MerchantService;
import nitpicksy.bank.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.security.NoSuchAlgorithmException;


@Validated
@RestController
@RequestMapping(value = "/api/payments", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {

    private PaymentService paymentService;

    private PaymentRequestMapper paymentRequestMapper;

    private MerchantService merchantService;

    @PostMapping(value = "/pay", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentResponseDTO> pay(@Valid @RequestBody PaymentRequestDTO paymentRequestDTO) throws NoSuchAlgorithmException {
        merchantService.findByMerchantIdAndPassword(paymentRequestDTO.getPaymentDetails().getMerchantId(),
                paymentRequestDTO.getPaymentDetails().getMerchantPassword());

        return new ResponseEntity<>(paymentService.pay(paymentRequestMapper.toEntity(paymentRequestDTO)), HttpStatus.OK);
    }

    @PostMapping(value = "/confirm/{paymentId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> confirmPayment(@PathVariable @NotNull @Positive(message = "Payment id must be positive.") Long paymentId,
                                                 @Valid @RequestBody ConfirmPaymentDTO confirmPaymentDTO) throws NoSuchAlgorithmException {
        return new ResponseEntity<>(paymentService.confirmPayment(confirmPaymentDTO, paymentId), HttpStatus.OK);
    }

    @Autowired
    public PaymentController(PaymentService paymentService, PaymentRequestMapper paymentRequestMapper, MerchantService merchantService) {
        this.paymentService = paymentService;
        this.paymentRequestMapper = paymentRequestMapper;
        this.merchantService = merchantService;
    }
}

