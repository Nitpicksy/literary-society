package nitpicksy.paymentgateway.serviceimpl;

import feign.FeignException;
import nitpicksy.paymentgateway.client.ZuulClient;
import nitpicksy.paymentgateway.dto.request.SubscriptionPlanDTO;
import nitpicksy.paymentgateway.dto.request.SubscriptionPlanToPaypalDTO;
import nitpicksy.paymentgateway.exceptionHandler.InvalidDataException;
import nitpicksy.paymentgateway.model.*;
import nitpicksy.paymentgateway.repository.SubscriptionRepository;
import nitpicksy.paymentgateway.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final String CLASS_PATH = this.getClass().getCanonicalName();
    private final String CLASS_NAME = this.getClass().getSimpleName();

    @Value("${API_GATEWAY_URL}")
    private String apiGatewayURL;

    private SubscriptionRepository subscriptionRepository;

    private UserService userService;

    private MerchantService merchantService;

    private DataForPaymentService dataForPaymentService;

    private LogService logService;

    private ZuulClient zuulClient;

    @Override
    public String createSubscriptionPlan(SubscriptionPlanDTO subscriptionPlanDTO) {
        Company company = userService.getAuthenticatedCompany();
        Merchant merchant = merchantService.findByNameAndCompany(subscriptionPlanDTO.getMerchantName(), company.getId());

        DataForPayment merchantClientId = dataForPaymentService.findByAttribute(merchant.getId(),
                "paypal", "merchantClientId");
        DataForPayment merchantClientSecret = dataForPaymentService.findByAttribute(merchant.getId(),
                "paypal", "merchantClientSecret");

        SubscriptionPlanToPaypalDTO planToPaypalDTO = new SubscriptionPlanToPaypalDTO();
        planToPaypalDTO.setMerchantClientId(merchantClientId.getAttributeValue());
        planToPaypalDTO.setMerchantClientSecret(merchantClientSecret.getAttributeValue());
        planToPaypalDTO.setProductName(subscriptionPlanDTO.getProductName());
        planToPaypalDTO.setProductType(subscriptionPlanDTO.getProductType());
        planToPaypalDTO.setProductCategory(subscriptionPlanDTO.getProductCategory());
        planToPaypalDTO.setPlanName(subscriptionPlanDTO.getPlanName());
        planToPaypalDTO.setPlanDescription(subscriptionPlanDTO.getPlanDescription());
        planToPaypalDTO.setPrice(subscriptionPlanDTO.getPrice());
        planToPaypalDTO.setFrequencyUnit(subscriptionPlanDTO.getFrequencyUnit());
        planToPaypalDTO.setFrequencyCount(subscriptionPlanDTO.getFrequencyCount());


        String paypalPlanId = null;
        try {
            paypalPlanId = zuulClient.createSubscriptionPlan(URI.create(apiGatewayURL + "/paypal"), planToPaypalDTO);
        } catch (RuntimeException e) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME,
                    "TRA", "Could not forward request to PayPal service."));
            throw new InvalidDataException("Subscription plan creating has failed. Please try again later.", HttpStatus.BAD_REQUEST);
        }

        SubscriptionPlan subscriptionPlan = new SubscriptionPlan(null, paypalPlanId, subscriptionPlanDTO.getId(), company.getCommonName());
        subscriptionRepository.save(subscriptionPlan);

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CSP", String.format("Subscription plan with paypalPlanId=%s successfully created.", paypalPlanId)));

        return paypalPlanId;
    }

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, UserService userService, MerchantService merchantService,
                                   DataForPaymentService dataForPaymentService, LogService logService, ZuulClient zuulClient) {
        this.subscriptionRepository = subscriptionRepository;
        this.userService = userService;
        this.merchantService = merchantService;
        this.dataForPaymentService = dataForPaymentService;
        this.logService = logService;
        this.zuulClient = zuulClient;
    }
}
