package nitpicksy.paymentgateway.service;

import nitpicksy.paymentgateway.dto.request.CancelSubscriptionDTO;
import nitpicksy.paymentgateway.dto.request.SubscriptionDTO;
import nitpicksy.paymentgateway.dto.request.SubscriptionPlanDTO;

public interface SubscriptionService {

    String createSubscriptionPlan(SubscriptionPlanDTO subscriptionPlanDTO);

    String subscribe(SubscriptionDTO subscriptionDTO);

    void unsubscribe(CancelSubscriptionDTO cancelSubscriptionDTO);

}
