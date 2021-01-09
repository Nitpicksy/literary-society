package nitpicksy.paypalservice.repository;

import nitpicksy.paypalservice.model.PaymentRequest;
import nitpicksy.paypalservice.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {

    SubscriptionPlan findOneByPlanId(String planId);

}
