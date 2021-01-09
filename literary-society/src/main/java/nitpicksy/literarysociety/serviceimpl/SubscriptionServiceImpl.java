package nitpicksy.literarysociety.serviceimpl;

import feign.FeignException;
import nitpicksy.literarysociety.client.ZuulClient;
import nitpicksy.literarysociety.constants.RoleConstants;
import nitpicksy.literarysociety.dto.request.SubscriptionDTO;
import nitpicksy.literarysociety.dto.request.SubscriptionPlanDTO;
import nitpicksy.literarysociety.exceptionHandler.InvalidDataException;
import nitpicksy.literarysociety.mapper.SubscriptionPlanDtoMapper;
import nitpicksy.literarysociety.model.*;
import nitpicksy.literarysociety.repository.MembershipRepository;
import nitpicksy.literarysociety.repository.SubscriptionPlanRepository;
import nitpicksy.literarysociety.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final String CLASS_PATH = this.getClass().getCanonicalName();
    private final String CLASS_NAME = this.getClass().getSimpleName();

    private SubscriptionPlanRepository subscriptionPlanRepository;

    private MembershipService membershipService;

    private MerchantService merchantService;

    private JWTTokenService jwtTokenService;

    private UserService userService;

    private LogService logService;

    private ZuulClient zuulClient;

    private SubscriptionPlanDtoMapper planDtoMapper;

    @Override
    public SubscriptionPlan createSubscriptionPlan(SubscriptionPlanDTO subscriptionPlanDTO) {
        SubscriptionPlan savedPlan = subscriptionPlanRepository.save(planDtoMapper.toEntity(subscriptionPlanDTO));

        Merchant ourMerchant = merchantService.findOurMerchant();
        String jwtToken = jwtTokenService.getToken();

        subscriptionPlanDTO.setId(savedPlan.getId());
        subscriptionPlanDTO.setMerchantName(ourMerchant.getName());

        try {
            zuulClient.createSubscriptionPlan("Bearer " + jwtToken, subscriptionPlanDTO);
        } catch (RuntimeException e) {
            subscriptionPlanRepository.deleteById(savedPlan.getId());

            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CSP",
                    String.format("Create subscription plan '%s' forwarding has failed", subscriptionPlanDTO.getPlanName())));
            throw new InvalidDataException("Subscription plan creating has failed. Please try again later.", HttpStatus.BAD_REQUEST);
        }

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CSP",
                String.format("Subscription plan '%s' with id=%s successfully created.", subscriptionPlanDTO.getPlanName(), subscriptionPlanDTO.getId())));

        return savedPlan;
    }

    @Override
    public SubscriptionPlan getSubscriptionPlan(String nameSubstring) {
        return subscriptionPlanRepository.findOneByPlanNameContainingIgnoringCase(nameSubstring);
    }

    @Override
    public String subscribe(Long planId) {
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findOneById(planId);
        if (subscriptionPlan == null) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "SUB",
                    "Request to subscribe failed. Invalid plan id sent."));
            throw new InvalidDataException("Plan you want to subscribe to is not available.", HttpStatus.BAD_REQUEST);
        }

        User user = userService.getAuthenticatedUser();

        Membership membership = membershipService.findUserSubscription(user.getUserId());
        if (membership != null) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "SUB",
                    "Request to subscribe failed. Already subscribed."));
            throw new InvalidDataException("You are already subscribed.", HttpStatus.BAD_REQUEST);
        }

        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setPlanId(planId);
        if (membershipService.checkIfUserMembershipIsValid(user.getUserId())) {
            Membership oneTimeMembership = membershipService.findLatestUserMembership(user.getUserId());
            subscriptionDTO.setStartDate(oneTimeMembership.getExpirationDate().plusDays(1));
        }

        String jwtToken = jwtTokenService.getToken();
        String redirectURL = null;
        try {
            redirectURL = zuulClient.subscribe("Bearer " + jwtToken, subscriptionDTO);
        } catch (RuntimeException e) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "SUP",
                    "Forwarding request to subscribe has failed"));
            throw new InvalidDataException("Request to subscribe failed. Please try again later.", HttpStatus.BAD_REQUEST);
        }

        return redirectURL;
    }

    @Override
    public void createMembership() {
        User user = userService.getAuthenticatedUser();

        Membership membership = membershipService.findUserSubscription(user.getUserId());
        if (membership != null) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "SUB",
                    "Request to subscribe failed. Already subscribed."));
            throw new InvalidDataException("You are already subscribed.", HttpStatus.BAD_REQUEST);
        }
        
        Merchant ourMerchant = merchantService.findOurMerchant();
        membershipService.createSubscriptionMembership(user, ourMerchant);
    }

    @Autowired
    public SubscriptionServiceImpl(SubscriptionPlanRepository subscriptionPlanRepository, MembershipService membershipService,
                                   MerchantService merchantService, JWTTokenService jwtTokenService, UserService userService,
                                   LogService logService, ZuulClient zuulClient, SubscriptionPlanDtoMapper planDtoMapper) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.membershipService = membershipService;
        this.merchantService = merchantService;
        this.jwtTokenService = jwtTokenService;
        this.userService = userService;
        this.logService = logService;
        this.zuulClient = zuulClient;
        this.planDtoMapper = planDtoMapper;
    }
}
