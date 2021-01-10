package nitpicksy.paypalservice.controller;

import nitpicksy.paypalservice.dto.response.TransactionResponseDTO;
import nitpicksy.paypalservice.mapper.TransactionMapper;
import nitpicksy.paypalservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {

    private TransactionService transactionService;

    private TransactionMapper transactionMapper;

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> findAll() {
        return new ResponseEntity<>(transactionService.findAll().stream()
                .map(transactionMapper::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @Autowired
    public TransactionController(TransactionService transactionService, TransactionMapper transactionMapper) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
    }
}