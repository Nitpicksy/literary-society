package nitpicksy.paymentgateway.controller;

import nitpicksy.paymentgateway.client.ZuulClient;
import nitpicksy.paymentgateway.dto.request.PaymentDataRequestDTO;
import nitpicksy.paymentgateway.dto.response.MerchantResponseDTO;
import nitpicksy.paymentgateway.dto.response.PaymentDataResponseDTO;
import nitpicksy.paymentgateway.mapper.MerchantResponseMapper;
import nitpicksy.paymentgateway.mapper.PaymentDataRequestMapper;
import nitpicksy.paymentgateway.mapper.PaymentDataResponseMapper;
import nitpicksy.paymentgateway.model.Company;
import nitpicksy.paymentgateway.model.Log;
import nitpicksy.paymentgateway.model.Merchant;
import nitpicksy.paymentgateway.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

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

    private LogService logService;

    @Value("${API_GATEWAY_URL}")
    private String apiGatewayURL;

    private CompanyService companyService;

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private MerchantResponseMapper merchantResponseMapper;

    @GetMapping("/{name}/payment-data")
    public ResponseEntity<String> getPaymentData(@PathVariable String name) {
        Company company = userService.getAuthenticatedCompany();

        Merchant merchant = merchantService.findByNameAndCompany(name, company.getId());
        if (merchant == null) {
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "MER",
                    String.format("In company %s merchant %s doesn't exist, creating new one.", company.getId(), name)));
            merchant = merchantService.save(new Merchant(name, company));
        }
        String url = getLocalhostURL() + "payment-data?company=" + company.getId() + "&merchant=" + merchant.getId();

        return new ResponseEntity<>(url, HttpStatus.OK);
    }

    @GetMapping("/payment-data")
    public ResponseEntity<List<PaymentDataResponseDTO>> getPaymentData(@RequestParam Long companyId, @RequestParam Long merchantId) {
        Merchant merchant = merchantService.findByIdAndCompany(merchantId, companyId);
        if (merchant == null) {
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "MER",
                    String.format("In company %s merchant %s doesn't exist", companyId, merchantId)));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(paymentDataResponseMapper.convert(paymentMethodService.getPaymentMethodsWithoutDataForPayment(merchant)), HttpStatus.OK);
    }

    @PostMapping("/payment-data")
    public ResponseEntity<String> supportPaymentMethods(@Valid @RequestBody List<PaymentDataRequestDTO> listPaymentDataRequestDTO, @RequestParam Long companyId, @RequestParam Long merchantId) throws NoSuchAlgorithmException {
        Merchant merchant = merchantService.findByIdAndCompany(merchantId, companyId);
        if (merchant == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        dataForPaymentService.save(paymentDataRequestMapper.convert(listPaymentDataRequestDTO, merchant));
        merchant.setSupportsPaymentMethods(true);
        merchantService.save(merchant);

        String redirectURL = null;
        try {
            redirectURL = zuulClient.supportPaymentMethods(URI.create(apiGatewayURL + '/' + merchant.getCompany().getCommonName()), merchant.getName());
        } catch (RuntimeException e) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "MER", "Could not notify " + merchant.getCompany().getCommonName()));
        }
        return new ResponseEntity<>(redirectURL, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody String merchantName) {
        Company company = userService.getAuthenticatedCompany();
        if (company == null) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ADDM", "Company not found"));
            return new ResponseEntity<>(HttpStatus.OK);
        }
        if (merchantService.findByNameAndCompany(merchantName, company.getId()) == null) {
            merchantService.save(new Merchant(merchantName, company));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MerchantResponseDTO>> findAll() {
        Company company = userService.getAuthenticatedCompany();
        if (company == null) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "GETM", "Company not found"));
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(merchantService.findByCompany(company.getId()).stream()
                .map(merchantResponseMapper::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @Scheduled(cron = "0 40 0 * * ?")
    @Async
    public void synchronizeMerchants() {
        List<Company> companyList = companyService.findAllApproved();
        for (Company company : companyList) {
            try {
                List<String> merchants = zuulClient.getAllMerchants(URI.create(apiGatewayURL + '/' + company.getCommonName()));
                Long companyId = company.getId();
                for (String merchantName : merchants) {
                    Merchant merchant = merchantService.findByNameAndCompany(merchantName, companyId);
                    if (merchant == null) {
                        merchantService.save(new Merchant(merchantName, company));
                    }

                }
                logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "SYNC",
                        "Merchants are successfully synchronized - " + company.getCompanyName()));
            } catch (RuntimeException e) {
                logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "SYNC", "Forwarding request to synchronize merchants has failed "));
            }
        }
    }

    private String getLocalhostURL() {
        return environment.getProperty("LOCALHOST_URL");
    }

    @Autowired
    public MerchantController(MerchantService merchantService, UserService userService, Environment environment, PaymentMethodService paymentMethodService,
                              PaymentDataResponseMapper paymentDataResponseMapper, PaymentDataRequestMapper paymentDataRequestMapper,
                              DataForPaymentService dataForPaymentService, ZuulClient zuulClient, LogService logService, CompanyService companyService, MerchantResponseMapper merchantResponseMapper) {
        this.merchantService = merchantService;
        this.userService = userService;
        this.environment = environment;
        this.paymentMethodService = paymentMethodService;
        this.paymentDataResponseMapper = paymentDataResponseMapper;
        this.paymentDataRequestMapper = paymentDataRequestMapper;
        this.dataForPaymentService = dataForPaymentService;
        this.zuulClient = zuulClient;
        this.logService = logService;
        this.companyService = companyService;
        this.merchantResponseMapper = merchantResponseMapper;
    }
}
