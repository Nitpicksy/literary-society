package nitpicksy.paypalservice.controller;

import nitpicksy.paypalservice.dto.response.TransactionResponseDTO;
import nitpicksy.paypalservice.mapper.TransactionMapper;
import nitpicksy.paypalservice.service.TransactionService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.util.Base64;
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