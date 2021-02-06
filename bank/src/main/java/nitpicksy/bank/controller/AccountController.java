package nitpicksy.bank.controller;

import nitpicksy.bank.dto.request.AccountRequestDTO;
import nitpicksy.bank.dto.request.MerchantRequestDTO;
import nitpicksy.bank.dto.request.PayRequestDTO;
import nitpicksy.bank.dto.request.PaymentRequestDTO;
import nitpicksy.bank.dto.response.AccountResponseDTO;
import nitpicksy.bank.dto.response.MerchantResponseDTO;
import nitpicksy.bank.dto.response.PayResponseDTO;
import nitpicksy.bank.dto.response.PaymentResponseDTO;
import nitpicksy.bank.mapper.AccountRequestMapper;
import nitpicksy.bank.mapper.AccountResponseMapper;
import nitpicksy.bank.model.Transaction;
import nitpicksy.bank.service.AccountService;
import nitpicksy.bank.service.PaymentService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    private PaymentService paymentService;

    private AccountService accountService;

    private AccountResponseMapper accountResponseMapper;

    private AccountRequestMapper accountRequestMapper;

    @PostMapping(value = "/pay", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PayResponseDTO> pay(@Valid @RequestBody PayRequestDTO paymentRequestDTO) {
        Transaction transaction = paymentService.payClientBank(paymentRequestDTO);
        PayResponseDTO responseDTO = new PayResponseDTO(paymentRequestDTO.getAcquirerOrderId(), paymentRequestDTO.getAcquirerTimestamp(),
                transaction.getId(), new Timestamp(DateTime.now().getMillis()), transaction.getStatus());

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> findAll() {
        return new ResponseEntity<>(accountService.findAll().stream()
                .map(company -> accountResponseMapper.toDto(company)).collect(Collectors.toList()), HttpStatus.OK);
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponseDTO> create(@RequestBody @Valid AccountRequestDTO accountRequestDTO) throws NoSuchAlgorithmException {
        return new ResponseEntity<>(accountResponseMapper.toDto(accountService.save(accountRequestMapper.toEntity(accountRequestDTO))), HttpStatus.OK);
    }

    @Autowired
    public AccountController(PaymentService paymentService, AccountService accountService, AccountResponseMapper accountResponseMapper, AccountRequestMapper accountRequestMapper) {
        this.paymentService = paymentService;
        this.accountService = accountService;
        this.accountResponseMapper = accountResponseMapper;
        this.accountRequestMapper = accountRequestMapper;
    }
}
