package nitpicksy.pcc.controller;

import nitpicksy.pcc.dto.PCCRequestDTO;
import nitpicksy.pcc.dto.PayResponseDTO;
import nitpicksy.pcc.service.INCodeBookService;
import nitpicksy.pcc.service.TransactionService;
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

@Validated
@RestController
@RequestMapping(value = "/api/payments", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {

    private TransactionService transactionService;

    @PostMapping(value = "/pay",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PayResponseDTO> pay(@Valid @RequestBody PCCRequestDTO pccRequestDTO) {
        return new ResponseEntity<>(transactionService.pay(pccRequestDTO), HttpStatus.OK);
    }

    @Autowired
    public PaymentController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
}
