package nitpicksy.paypalservice.service;

import nitpicksy.paypalservice.dto.request.SubscriptionDTO;
import nitpicksy.paypalservice.model.SubscriptionPlan;

public interface SubscriptionService {

    String createBillingPlan(SubscriptionPlan subscriptionPlan);

    String createSubscription(SubscriptionDTO subscriptionDTO);

}
