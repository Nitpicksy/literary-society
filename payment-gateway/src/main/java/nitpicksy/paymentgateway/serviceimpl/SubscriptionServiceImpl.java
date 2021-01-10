package nitpicksy.paymentgateway.serviceimpl;

import nitpicksy.paymentgateway.client.ZuulClient;
import nitpicksy.paymentgateway.dto.request.*;
import nitpicksy.paymentgateway.exceptionHandler.InvalidDataException;
import nitpicksy.paymentgateway.model.*;
import nitpicksy.paymentgateway.repository.SubscriptionRepository;
import nitpicksy.paymentgateway.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URI;

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
    public String createSubscriptionPlan(SubscriptionPlanDTO planDTO) {
        Company company = userService.getAuthenticatedCompany();
        Merchant merchant = merchantService.findByNameAndCompany(planDTO.getMerchantName(), company.getId());

        DataForPayment merchantClientId = dataForPaymentService.findByAttribute(merchant.getId(),
                "paypal", "merchantClientId");
        DataForPayment merchantClientSecret = dataForPaymentService.findByAttribute(merchant.getId(),
                "paypal", "merchantClientSecret");

        SubscriptionPlanToPaypalDTO planToPaypalDTO = new SubscriptionPlanToPaypalDTO(merchantClientId.getAttributeValue(),
                merchantClientSecret.getAttributeValue(), planDTO.getProductName(), planDTO.getProductType(),
                planDTO.getProductCategory(), planDTO.getPlanName(), planDTO.getPlanDescription(), planDTO.getPrice(),
                planDTO.getFrequencyUnit(), planDTO.getFrequencyCount(), planDTO.getSuccessURL(), planDTO.getCancelURL());

        String paypalPlanId = null;
        try {
            paypalPlanId = zuulClient.createSubscriptionPlan(URI.create(apiGatewayURL + "/paypal"), planToPaypalDTO);
        } catch (RuntimeException e) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME,
                    "SUB", "Could not forward request to PayPal service."));
            throw new InvalidDataException("Subscription plan creating has failed. Please try again later.", HttpStatus.BAD_REQUEST);
        }

        SubscriptionPlan subscriptionPlan = new SubscriptionPlan(null, paypalPlanId, planDTO.getId(), company.getCommonName());
        subscriptionRepository.save(subscriptionPlan);

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CSP",
                String.format("Subscription plan with paypalPlanId=%s successfully created.", paypalPlanId)));

        return paypalPlanId;
    }

    @Override
    public String subscribe(SubscriptionDTO subscriptionDTO) {
        Company company = userService.getAuthenticatedCompany();
        SubscriptionPlan plan = subscriptionRepository.findByCompanyCommonNameAndCompanyPlanId(company.getCommonName(), subscriptionDTO.getPlanId());

        SubscriptionToPaypalDTO subscriptionToPaypalDTO = new SubscriptionToPaypalDTO(plan.getPaypalPlanId(), subscriptionDTO.getStartDate());
        String redirectURL = null;
        try {
            redirectURL = zuulClient.subscribe(URI.create(apiGatewayURL + "/paypal"), subscriptionToPaypalDTO);
        } catch (RuntimeException e) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME,
                    "SUB", "Could not forward request to PayPal service."));
            throw new InvalidDataException("Request to subscribe has failed. Please try again later.", HttpStatus.BAD_REQUEST);
        }

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "SUB",
                String.format("Subscription for plan with paypalPlanId=%s successfully created.", plan.getPaypalPlanId())));

        return redirectURL;
    }

    @Override
    public void unsubscribe(CancelSubscriptionDTO cancelDTO) {
        Company company = userService.getAuthenticatedCompany();
        SubscriptionPlan plan = subscriptionRepository.findByCompanyCommonNameAndCompanyPlanId(company.getCommonName(), cancelDTO.getPlanId());

        CancelSubscriptionToPaypalDTO cancelToPaypalDTO = new CancelSubscriptionToPaypalDTO(plan.getPaypalPlanId(), cancelDTO.getSubscriptionId());
        try {
            zuulClient.unsubscribe(URI.create(apiGatewayURL + "/paypal"), cancelToPaypalDTO);
        } catch (RuntimeException e) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME,
                    "UNS", "Could not forward request to PayPal service."));
            throw new InvalidDataException("Request to unsubscribe has failed. Please try again later.", HttpStatus.BAD_REQUEST);
        }

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "UNS",
                String.format("Subscription for plan with paypalPlanId=%s successfully canceled.", plan.getPaypalPlanId())));
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
