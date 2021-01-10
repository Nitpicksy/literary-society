package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.dto.request.SubscriptionPlanDTO;
import nitpicksy.literarysociety.dto.response.SubscriptionPlanResponseDTO;
import nitpicksy.literarysociety.model.SubscriptionPlan;

public interface SubscriptionService {

    SubscriptionPlan createSubscriptionPlan(SubscriptionPlanDTO subscriptionPlanDTO);

    SubscriptionPlanResponseDTO getSubscriptionPlan(String nameSubstring);

    String subscribe(Long planId);

    void createMembership(String subscriptionId);

    void unsubscribe(Long planId);

}
