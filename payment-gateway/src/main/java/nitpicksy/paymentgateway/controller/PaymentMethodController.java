package nitpicksy.paymentgateway.controller;

import nitpicksy.paymentgateway.dto.both.PaymentMethodDTO;
import nitpicksy.paymentgateway.dto.request.CreatePaymentMethodDTO;
import nitpicksy.paymentgateway.dto.response.PaymentMethodDataDTO;
import nitpicksy.paymentgateway.dto.response.PaymentMethodResponseDTO;
import nitpicksy.paymentgateway.mapper.*;
import nitpicksy.paymentgateway.model.PaymentMethod;
import nitpicksy.paymentgateway.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
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
    public ResponseEntity<List<PaymentMethodResponseDTO>> getPaymentMethodsForMerchant(@PathVariable("orderId") @Positive(message = "Id must be positive.") Long orderId) {
        return new ResponseEntity<>(paymentMethodService.findMerchantPaymentMethods(orderId).stream()
                .map(paymentMethod -> paymentMethodMapper.toDto(paymentMethod)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentMethod> registerPaymentMethod(@Valid @RequestBody CreatePaymentMethodDTO createPaymentMethodDTO) {
        //sacuvaj multipart file i sacuvaj njegov naziv
//        try {
//            pdfDocumentService.upload(pdfFile, book);
//        } catch (IOException e) {
//            throw new InvalidDataException("Manuscript could not be uploaded. Please try again later.", HttpStatus.BAD_REQUEST);
//        }
        PaymentMethod paymentMethod = paymentMethodService.registerPaymentMethod(mainDataMapper.toEntity(createPaymentMethodDTO.getMainData()),
                dataMapper.convertList(createPaymentMethodDTO.getPaymentData()));

        return new ResponseEntity<>(paymentMethod, HttpStatus.OK);
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
    public ResponseEntity<PaymentMethodDataDTO> changePaymentMethodStatus(@PathVariable @Positive Long id,
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