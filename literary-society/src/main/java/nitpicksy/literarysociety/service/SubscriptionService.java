package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.dto.request.SubscriptionPlanDTO;
import nitpicksy.literarysociety.model.SubscriptionPlan;

import java.util.List;

public interface SubscriptionService {

    SubscriptionPlan createSubscriptionPlan(SubscriptionPlanDTO subscriptionPlanDTO);

    SubscriptionPlan getSubscriptionPlan(String nameSubstring);

    String subscribe(Long planId);

    void createMembership();

}
