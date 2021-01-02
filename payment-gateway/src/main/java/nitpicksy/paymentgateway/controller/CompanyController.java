package nitpicksy.paymentgateway.controller;

import nitpicksy.paymentgateway.dto.both.PaymentMethodDTO;
import nitpicksy.paymentgateway.dto.request.CompanyDataDTO;
import nitpicksy.paymentgateway.dto.response.CompanyResponseDTO;
import nitpicksy.paymentgateway.dto.response.PaymentMethodDataDTO;
import nitpicksy.paymentgateway.exceptionHandler.InvalidDataException;
import nitpicksy.paymentgateway.mapper.CompanyDataMapper;
import nitpicksy.paymentgateway.mapper.CompanyResponseMapper;
import nitpicksy.paymentgateway.model.Company;
import nitpicksy.paymentgateway.service.CompanyService;
import nitpicksy.paymentgateway.utils.CertificateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping(value = "/api/companies", produces = MediaType.APPLICATION_JSON_VALUE)
public class CompanyController {

    private CompanyService companyService;

    private CompanyDataMapper companyDataMapper;

    private CompanyResponseMapper companyResponseMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CompanyResponseDTO> addCompany(@RequestPart @Valid CompanyDataDTO companyData, @RequestPart @NotNull MultipartFile certificate,
                                                         @RequestPart @NotEmpty(message = "Supported payment methods must not be empty") List<PaymentMethodDTO> supportedPaymentMethods) {
        Company company = companyDataMapper.toEntity(companyData);
        company.setCertificateName(certificate.getOriginalFilename());

        Company savedCompany = companyService.addCompany(company, supportedPaymentMethods);

        try {
            if (CertificateUtils.saveCertFile(certificate) == null) {
                throw new InvalidDataException("Certificate name already in use. Please try with another one.", HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            throw new InvalidDataException("Certificate could not be uploaded. Please try again later.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(companyResponseMapper.toDto(savedCompany), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponseDTO>> findAll() {
        return new ResponseEntity<>(companyService.findAll().stream()
                .map(company -> companyResponseMapper.toDto(company)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CompanyResponseDTO> changeStatus(@PathVariable @Positive Long id,
                                                           @RequestParam @Pattern(regexp = "(?i)(approve|reject)$", message = "Status is not valid.") String status) {
        Company company = companyService.changeStatus(id, status);
        if (company == null) {
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(companyResponseMapper.toDto(company), HttpStatus.OK);
    }

    @Autowired
    public CompanyController(CompanyService companyService, CompanyDataMapper companyDataMapper,
                             CompanyResponseMapper companyResponseMapper) {
        this.companyService = companyService;
        this.companyDataMapper = companyDataMapper;
        this.companyResponseMapper = companyResponseMapper;
    }
}
