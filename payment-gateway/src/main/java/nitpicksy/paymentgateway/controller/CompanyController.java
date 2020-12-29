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
    public ResponseEntity<CompanyDataDTO> addCompany(@RequestPart @Valid CompanyDataDTO companyData, @RequestPart @NotNull MultipartFile certificate,
                                                     @RequestPart @NotEmpty(message = "Supported payment methods must not be empty") List<PaymentMethodDTO> supportedPaymentMethods) {
        try {
            CertificateUtils.saveCertFile(certificate);
        } catch (IOException e) {
            throw new InvalidDataException("Certificate could not be uploaded. Please try again later.", HttpStatus.BAD_REQUEST);
        }

        Company company = companyDataMapper.toEntity(companyData);
        company.setCertificateName(certificate.getOriginalFilename());

        return new ResponseEntity<>(companyDataMapper.toDto(companyService.addCompany(company, supportedPaymentMethods)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponseDTO>> findAll() {
        return new ResponseEntity<>(companyService.findAll().stream()
                .map(company -> companyResponseMapper.toDto(company)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @Autowired
    public CompanyController(CompanyService companyService, CompanyDataMapper companyDataMapper,
                             CompanyResponseMapper companyResponseMapper) {
        this.companyService = companyService;
        this.companyDataMapper = companyDataMapper;
        this.companyResponseMapper = companyResponseMapper;
    }
}
