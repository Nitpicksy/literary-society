package nitpicksy.literarysociety.controller;

import feign.FeignException;
import nitpicksy.literarysociety.client.ZuulClient;
import nitpicksy.literarysociety.dto.request.SubscriptionPlanDTO;
import nitpicksy.literarysociety.exceptionHandler.InvalidDataException;
import nitpicksy.literarysociety.model.Log;
import nitpicksy.literarysociety.model.Merchant;
import nitpicksy.literarysociety.model.PriceList;
import nitpicksy.literarysociety.service.JWTTokenService;
import nitpicksy.literarysociety.service.LogService;
import nitpicksy.literarysociety.service.MerchantService;
import nitpicksy.literarysociety.service.PriceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/subscriptions")
public class SubscriptionController {

    private final String CLASS_PATH = this.getClass().getCanonicalName();
    private final String CLASS_NAME = this.getClass().getSimpleName();

    private MerchantService merchantService;

    private PriceListService priceListService;

    private JWTTokenService jwtTokenService;

    private LogService logService;

    private ZuulClient zuulClient;

    @GetMapping(value = "/create-plans")
    public ResponseEntity<Void> createSubscriptionPlans() {
        PriceList currentPriceList = priceListService.findLatestPriceList();
        Merchant ourMerchant = merchantService.findOurMerchant();
        String jwtToken = jwtTokenService.getToken();

        SubscriptionPlanDTO writerMembership = new SubscriptionPlanDTO(1L, ourMerchant.getName(), "Book Publishing Option", "DIGITAL",
                "BOOKS_MANUSCRIPTS", "Writer Membership", "Create publication requests and publish books via Literary Society.",
                currentPriceList.getMembershipForWriter(), "MONTH", 3);
        try {
            zuulClient.createSubscriptionPlan("Bearer " + jwtToken, writerMembership);
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CSP",
                    String.format("Subscription plan '%s' with id=%s successfully created.", writerMembership.getPlanName(), writerMembership.getId())));
        } catch (RuntimeException e) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME,
                    "TRA", "Could not notify Payment Gateway"));
            throw new InvalidDataException("Subscription plan creating has failed. Please try again later.", HttpStatus.BAD_REQUEST);
        }

        SubscriptionPlanDTO readerMembership = new SubscriptionPlanDTO(2L, ourMerchant.getName(), "Book Purchasing Discount", "DIGITAL",
                "BOOKS_MANUSCRIPTS", "Reader Membership", "Buy books with discount from all Literary Society merchants.",
                currentPriceList.getMembershipForReader(), "MONTH", 1);
        try {
            zuulClient.createSubscriptionPlan("Bearer " + jwtToken, readerMembership);
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CSP",
                    String.format("Subscription plan '%s' with id=%s successfully created.", readerMembership.getPlanName(), readerMembership.getId())));
        } catch (FeignException.FeignClientException e) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CSP",
                    String.format("Create subscription plan '%s' forwarding has failed", readerMembership.getPlanName())));
            throw new InvalidDataException("Subscription plan creating has failed. Please try again later.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Autowired
    public SubscriptionController(MerchantService merchantService, PriceListService priceListService,
                                  JWTTokenService jwtTokenService, LogService logService, ZuulClient zuulClient) {
        this.merchantService = merchantService;
        this.priceListService = priceListService;
        this.jwtTokenService = jwtTokenService;
        this.logService = logService;
        this.zuulClient = zuulClient;
    }
}
