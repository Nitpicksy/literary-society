package nitpicksy.paymentgateway.controller;

import nitpicksy.paymentgateway.client.ZuulClient;
import nitpicksy.paymentgateway.dto.request.PaymentDataRequestDTO;
import nitpicksy.paymentgateway.dto.response.PaymentDataResponseDTO;
import nitpicksy.paymentgateway.mapper.PaymentDataRequestMapper;
import nitpicksy.paymentgateway.mapper.PaymentDataResponseMapper;
import nitpicksy.paymentgateway.model.Company;
import nitpicksy.paymentgateway.model.Merchant;
import nitpicksy.paymentgateway.service.DataForPaymentService;
import nitpicksy.paymentgateway.service.MerchantService;
import nitpicksy.paymentgateway.service.PaymentMethodService;
import nitpicksy.paymentgateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.env.Environment;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/merchants", produces = MediaType.APPLICATION_JSON_VALUE)
public class MerchantController {

    private MerchantService merchantService;

    private UserService userService;

    private Environment environment;

    private PaymentMethodService paymentMethodService;

    private PaymentDataResponseMapper paymentDataResponseMapper;

    private PaymentDataRequestMapper paymentDataRequestMapper;

    private DataForPaymentService dataForPaymentService;

    private ZuulClient zuulClient;

    @Value("${API_GATEWAY_URL}")
    private String apiGatewayURL;

    @GetMapping("/{name}/payment-data")
    public ResponseEntity<String> getPaymentData(@PathVariable String name) {
        Company company = userService.getAuthenticatedCompany();

        Merchant merchant = merchantService.findByNameAndCompany(name, company.getId());
        if(merchant == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String url = getLocalhostURL() + "payment-data?company="+company.getId()+"&merchant="+merchant.getId();

        return new ResponseEntity<>(url, HttpStatus.OK);
    }

    @GetMapping("/payment-data")
    public ResponseEntity<List<PaymentDataResponseDTO>> getPaymentData(@RequestParam Long companyId, @RequestParam Long merchantId) {
        Merchant merchant = merchantService.findByIdAndCompany(merchantId, companyId);
        if(merchant == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(paymentDataResponseMapper.convert(paymentMethodService.getPaymentMethodsWithoutDataForPayment(merchant)), HttpStatus.OK);
    }

    @PostMapping("/payment-data")
    public ResponseEntity<String> supportPaymentMethods(@Valid @RequestBody List<PaymentDataRequestDTO> listPaymentDataRequestDTO, @RequestParam Long companyId, @RequestParam Long merchantId) {
        Merchant merchant = merchantService.findByIdAndCompany(merchantId, companyId);
        if(merchant == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        dataForPaymentService.save(paymentDataRequestMapper.convert(listPaymentDataRequestDTO, merchant));
        String redirectURL = zuulClient.supportPaymentMethods(URI.create(apiGatewayURL + '/' + merchant.getCompany().getCommonName()), merchant.getName());

        return new ResponseEntity<>(redirectURL, HttpStatus.OK);
    }

    private String getLocalhostURL() {
        return environment.getProperty("LOCALHOST_URL");
    }

    @Autowired
    public MerchantController(MerchantService merchantService, UserService userService,Environment environment,PaymentMethodService paymentMethodService,
                              PaymentDataResponseMapper paymentDataResponseMapper,PaymentDataRequestMapper paymentDataRequestMapper,
                              DataForPaymentService dataForPaymentService,ZuulClient zuulClient) {
        this.merchantService = merchantService;
        this.userService = userService;
        this.environment = environment;
        this.paymentMethodService = paymentMethodService;
        this.paymentDataResponseMapper = paymentDataResponseMapper;
        this.paymentDataRequestMapper = paymentDataRequestMapper;
        this.dataForPaymentService = dataForPaymentService;
        this.zuulClient = zuulClient;
    }
}
