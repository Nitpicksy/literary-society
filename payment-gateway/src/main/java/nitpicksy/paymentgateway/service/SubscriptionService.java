package nitpicksy.paymentgateway.service;

import nitpicksy.paymentgateway.dto.request.SubscriptionPlanDTO;

public interface SubscriptionService {

    String createSubscriptionPlan(SubscriptionPlanDTO subscriptionPlanDTO);

}
