package nitpicksy.bank.controller;

import nitpicksy.bank.dto.request.PayRequestDTO;
import nitpicksy.bank.dto.request.PaymentRequestDTO;
import nitpicksy.bank.dto.response.PayResponseDTO;
import nitpicksy.bank.dto.response.PaymentResponseDTO;
import nitpicksy.bank.model.Transaction;
import nitpicksy.bank.service.AccountService;
import nitpicksy.bank.service.PaymentService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.sql.Timestamp;

@RestController
@RequestMapping(value = "/api/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    private PaymentService paymentService;

    @PostMapping(value = "/pay", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PayResponseDTO> pay(@Valid @RequestBody PayRequestDTO paymentRequestDTO) {
        Transaction transaction = paymentService.payClientBank(paymentRequestDTO);
        PayResponseDTO responseDTO = new PayResponseDTO(paymentRequestDTO.getAcquirerOrderId(), paymentRequestDTO.getAcquirerTimestamp(),
                transaction.getId(), new Timestamp(DateTime.now().getMillis()), transaction.getStatus());

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @Autowired
    public AccountController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
