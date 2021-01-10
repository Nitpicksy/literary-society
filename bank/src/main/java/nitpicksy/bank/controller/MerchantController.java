package nitpicksy.bank.controller;

import nitpicksy.bank.dto.request.MerchantRequestDTO;
import nitpicksy.bank.dto.response.MerchantResponseDTO;
import nitpicksy.bank.mapper.MerchantRequestMapper;
import nitpicksy.bank.mapper.MerchantResponseMapper;
import nitpicksy.bank.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping(value = "/api/merchants", produces = MediaType.APPLICATION_JSON_VALUE)
public class MerchantController {

    private MerchantService merchantService;

    private MerchantResponseMapper merchantResponseMapper;

    private MerchantRequestMapper merchantRequestMapper;

    @GetMapping
    public ResponseEntity<List<MerchantResponseDTO>> findAll() {
        return new ResponseEntity<>(merchantService.findAll().stream()
                .map(company -> merchantResponseMapper.toDto(company)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MerchantResponseDTO> create(@RequestBody @Valid MerchantRequestDTO merchantRequestDTO) throws NoSuchAlgorithmException {
        return new ResponseEntity<>(merchantResponseMapper.toDto(merchantService.save(merchantRequestMapper.toEntity(merchantRequestDTO))), HttpStatus.OK);
    }


    @Autowired
    public MerchantController(MerchantService merchantService, MerchantResponseMapper merchantResponseMapper, MerchantRequestMapper merchantRequestMapper) {
        this.merchantService = merchantService;
        this.merchantResponseMapper = merchantResponseMapper;
        this.merchantRequestMapper = merchantRequestMapper;
    }
}
