package nitpicksy.literarysociety.controller;


import nitpicksy.literarysociety.dto.response.TransactionDTO;
import nitpicksy.literarysociety.mapper.TransactionDtoMapper;
import nitpicksy.literarysociety.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping(value = "/api/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {

    private TransactionService transactionService;

    private TransactionDtoMapper transactionDtoMapper;

    @GetMapping(value = "{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDTO> getTransaction(@Positive @PathVariable Long id) {
        return new ResponseEntity<>(transactionDtoMapper.toDto(transactionService.findById(id)), HttpStatus.OK);
    }

    @Autowired
    public TransactionController(TransactionService transactionService, TransactionDtoMapper transactionDtoMapper) {
        this.transactionService = transactionService;
        this.transactionDtoMapper = transactionDtoMapper;
    }
}