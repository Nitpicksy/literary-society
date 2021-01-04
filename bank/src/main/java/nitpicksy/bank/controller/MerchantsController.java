package nitpicksy.bank.controller;

import nitpicksy.bank.dto.MerchantDTO;
import nitpicksy.bank.mapper.MerchantMapper;
import nitpicksy.bank.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping(value = "/api/merchants", produces = MediaType.APPLICATION_JSON_VALUE)
public class MerchantsController {

    private MerchantService merchantService;

    private MerchantMapper merchantMapper;

    @GetMapping
    public ResponseEntity<List<MerchantDTO>> findAll() {
        return new ResponseEntity<>(merchantService.findAll().stream()
                .map(company -> merchantMapper.toDto(company)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @Autowired
    public MerchantsController(MerchantService merchantService, MerchantMapper merchantMapper) {
        this.merchantService = merchantService;
        this.merchantMapper = merchantMapper;
    }
}
