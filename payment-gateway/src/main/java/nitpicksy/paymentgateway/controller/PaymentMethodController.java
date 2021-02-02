package nitpicksy.paymentgateway.controller;

import nitpicksy.paymentgateway.dto.both.PaymentMethodDTO;
import nitpicksy.paymentgateway.dto.request.CompanyDataDTO;
import nitpicksy.paymentgateway.dto.request.CreatePaymentMethodDTO;
import nitpicksy.paymentgateway.dto.request.CreatePaymentMethodMainDataDTO;
import nitpicksy.paymentgateway.dto.request.PaymentDataDTO;
import nitpicksy.paymentgateway.dto.response.PaymentMethodDataDTO;
import nitpicksy.paymentgateway.dto.response.PaymentMethodResponseDTO;
import nitpicksy.paymentgateway.exceptionHandler.InvalidDataException;
import nitpicksy.paymentgateway.mapper.*;
import nitpicksy.paymentgateway.model.Company;
import nitpicksy.paymentgateway.model.PaymentMethod;
import nitpicksy.paymentgateway.service.PaymentMethodService;
import nitpicksy.paymentgateway.utils.CertificateUtils;
import nitpicksy.paymentgateway.utils.TrustStoreUtils;
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
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping(value = "/api/payment-methods", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentMethodController {

    private PaymentMethodService paymentMethodService;

    private PaymentMethodMapper paymentMethodMapper;

    private DataMapper dataMapper;

    private CreatePaymentMethodMainDataMapper mainDataMapper;

    private PaymentMethodDataMapper paymentMethodDataMapper;

    private PaymentMethodDtoMapper paymentMethodDtoMapper;

    @GetMapping("/{orderId}")
    public ResponseEntity<List<PaymentMethodResponseDTO>> getPaymentMethodsForMerchant(@PathVariable("orderId") @NotNull @Positive(message = "Id must be positive.") Long orderId) {
        return new ResponseEntity<>(paymentMethodService.findMerchantPaymentMethods(orderId).stream()
                .map(paymentMethod -> paymentMethodMapper.toDto(paymentMethod)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PaymentMethod> registerPaymentMethod(@RequestPart @Valid CreatePaymentMethodMainDataDTO mainData, @RequestPart @NotNull MultipartFile certificate,
                                                               @RequestPart @NotEmpty(message = "Payment data list must not be empty") List<PaymentDataDTO> paymentDataList) {
        PaymentMethod paymentMethod = mainDataMapper.toEntity(mainData);
        paymentMethod.setCertificateName(certificate.getOriginalFilename());

        PaymentMethod savedPaymentMethod = paymentMethodService.registerPaymentMethod(paymentMethod,
                dataMapper.convertList(paymentDataList));

        try {
            if (CertificateUtils.saveCertFile(certificate) == null) {
                throw new InvalidDataException("Certificate name already in use. Please try with another one.", HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            throw new InvalidDataException("Certificate could not be uploaded. Please try again later.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(savedPaymentMethod, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PaymentMethodDataDTO>> findAll() {
        return new ResponseEntity<>(paymentMethodService.findAll().stream()
                .map(paymentMethod -> paymentMethodDataMapper.toDto(paymentMethod)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "/approved")
    public ResponseEntity<List<PaymentMethodDTO>> findAllApproved() {
        return new ResponseEntity<>(paymentMethodService.findAllApproved().stream()
                .map(paymentMethod -> paymentMethodDtoMapper.toDto(paymentMethod)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PaymentMethodDataDTO> changePaymentMethodStatus(@PathVariable @NotNull @Positive Long id,
                                                                          @RequestParam @Pattern(regexp = "(?i)(approve|reject)$", message = "Status is not valid.") String status) {
        return new ResponseEntity<>(paymentMethodDataMapper.toDto(paymentMethodService.changePaymentMethodStatus(id, status)), HttpStatus.OK);
    }

    @Autowired
    public PaymentMethodController(PaymentMethodService paymentMethodService, PaymentMethodMapper paymentMethodMapper, DataMapper dataMapper,
                                   CreatePaymentMethodMainDataMapper mainDataMapper, PaymentMethodDataMapper paymentMethodDataMapper,
                                   PaymentMethodDtoMapper paymentMethodDtoMapper) {
        this.paymentMethodService = paymentMethodService;
        this.paymentMethodMapper = paymentMethodMapper;
        this.dataMapper = dataMapper;
        this.mainDataMapper = mainDataMapper;
        this.paymentMethodDataMapper = paymentMethodDataMapper;
        this.paymentMethodDtoMapper = paymentMethodDtoMapper;
    }
}