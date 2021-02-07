package nitpicksy.literarysociety2.service;

import nitpicksy.literarysociety2.dto.request.SubscriptionPlanDTO;
import nitpicksy.literarysociety2.dto.response.SubscriptionPlanResponseDTO;
import nitpicksy.literarysociety2.model.SubscriptionPlan;

public interface SubscriptionService {

    SubscriptionPlan createSubscriptionPlan(SubscriptionPlanDTO subscriptionPlanDTO);

    SubscriptionPlanResponseDTO getSubscriptionPlan(String nameSubstring);

    String subscribe(Long planId);

    void createMembership(String subscriptionId);

    void unsubscribe(Long planId);

}
