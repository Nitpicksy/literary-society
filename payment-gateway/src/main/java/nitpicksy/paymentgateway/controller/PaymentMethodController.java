package nitpicksy.paymentgateway.controller;

import nitpicksy.paymentgateway.dto.both.PaymentMethodDTO;
import nitpicksy.paymentgateway.dto.request.CompanyDataDTO;
import nitpicksy.paymentgateway.dto.request.CreatePaymentMethodDTO;
import nitpicksy.paymentgateway.dto.request.CreatePaymentMethodMainDataDTO;
import nitpicksy.paymentgateway.dto.request.PaymentDataDTO;
import nitpicksy.paymentgateway.dto.response.PaymentMethodDataDTO;
import nitpicksy.paymentgateway.dto.response.PaymentMethodResponseDTO;
import nitpicksy.paymentgateway.dto.response.SupportedAndAllPaymentMethods;
import nitpicksy.paymentgateway.exceptionHandler.InvalidDataException;
import nitpicksy.paymentgateway.mapper.*;
import nitpicksy.paymentgateway.model.ChangePaymentMethodsToken;
import nitpicksy.paymentgateway.model.Company;
import nitpicksy.paymentgateway.model.Log;
import nitpicksy.paymentgateway.model.Merchant;
import nitpicksy.paymentgateway.model.PaymentMethod;
import nitpicksy.paymentgateway.service.ChangePaymentMethodsTokenService;
import nitpicksy.paymentgateway.service.PaymentMethodService;
import nitpicksy.paymentgateway.service.UserService;
import nitpicksy.paymentgateway.utils.CertificateUtils;
import nitpicksy.paymentgateway.utils.TrustStoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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

    private UserService userService;

    private Environment environment;

    private ChangePaymentMethodsTokenService changePaymentMethodsTokenService;

    @GetMapping("/{orderId}")
    public ResponseEntity<List<PaymentMethodResponseDTO>> getPaymentMethodsForMerchant(@PathVariable("orderId") @Positive(message = "Id must be positive.") Long orderId) {
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

    @GetMapping(value = "/all-and-supported")
    public ResponseEntity<SupportedAndAllPaymentMethods> findSupportedAndAllPaymentMethods(@RequestParam @NotBlank String token) throws NoSuchAlgorithmException {
        SupportedAndAllPaymentMethods supportedAndAllPaymentMethods = new SupportedAndAllPaymentMethods();
        supportedAndAllPaymentMethods.setPaymentMethods(paymentMethodService.findAllApproved().stream()
                .map(paymentMethod -> paymentMethodDtoMapper.toDto(paymentMethod)).collect(Collectors.toList()));

        ChangePaymentMethodsToken changePaymentMethodsToken = changePaymentMethodsTokenService.verifyToken(token);
        supportedAndAllPaymentMethods.setSupportedMethods(changePaymentMethodsToken.getCompany().getPaymentMethods().stream()
                .map(paymentMethod -> paymentMethodDtoMapper.toDto(paymentMethod)).collect(Collectors.toList()));

        return new ResponseEntity<>(supportedAndAllPaymentMethods, HttpStatus.OK);
    }

    @PutMapping(value = "/company")
    public ResponseEntity<String> changeSupportPaymentMethods(@RequestBody @NotEmpty List<PaymentMethodDTO> paymentMethods,
                                                              @RequestParam String token) throws NoSuchAlgorithmException {
        ChangePaymentMethodsToken changePaymentMethodsToken = changePaymentMethodsTokenService.verifyToken(token);
        changePaymentMethodsTokenService.invalidateToken(changePaymentMethodsToken.getId());
        Company company = changePaymentMethodsToken.getCompany();
        List<PaymentMethod> listPaymentMethods = paymentMethods.stream()
                .map(paymentMethod -> paymentMethodService.findById(paymentMethod.getId())).collect(Collectors.toList());

        return new ResponseEntity<>(paymentMethodService.changeSupportPaymentMethods(listPaymentMethods, company), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PaymentMethodDataDTO> changePaymentMethodStatus(@PathVariable @Positive Long id,
                                                                          @RequestParam @Pattern(regexp = "(?i)(approve|reject)$", message = "Status is not valid.") String status) {
        return new ResponseEntity<>(paymentMethodDataMapper.toDto(paymentMethodService.changePaymentMethodStatus(id, status)), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> getPaymentData() throws NoSuchAlgorithmException {
        Company company = userService.getAuthenticatedCompany();
        if(company == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String token = changePaymentMethodsTokenService.generateToken(company);
        String url = getLocalhostURL() + "choose-payment-methods?token="+token;

        return new ResponseEntity<>(url, HttpStatus.OK);
    }

    private String getLocalhostURL() {
        return environment.getProperty("LOCALHOST_URL");
    }

    @Autowired
    public PaymentMethodController(PaymentMethodService paymentMethodService, PaymentMethodMapper paymentMethodMapper, DataMapper dataMapper,
                                   CreatePaymentMethodMainDataMapper mainDataMapper, PaymentMethodDataMapper paymentMethodDataMapper,
                                   PaymentMethodDtoMapper paymentMethodDtoMapper, UserService userService, Environment environment,
                                   ChangePaymentMethodsTokenService changePaymentMethodsTokenService) {
        this.paymentMethodService = paymentMethodService;
        this.paymentMethodMapper = paymentMethodMapper;
        this.dataMapper = dataMapper;
        this.mainDataMapper = mainDataMapper;
        this.paymentMethodDataMapper = paymentMethodDataMapper;
        this.paymentMethodDtoMapper = paymentMethodDtoMapper;
        this.userService = userService;
        this.environment = environment;
        this.changePaymentMethodsTokenService = changePaymentMethodsTokenService;
    }
}