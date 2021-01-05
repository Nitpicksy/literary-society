package nitpicksy.paypalservice.service;

import nitpicksy.paypalservice.dto.request.SubscriptionPlanDTO;

public interface SubscriptionService {

    String createBillingPlan(SubscriptionPlanDTO subscriptionPlanDTO);

    String createSubscription(String planId);

}
