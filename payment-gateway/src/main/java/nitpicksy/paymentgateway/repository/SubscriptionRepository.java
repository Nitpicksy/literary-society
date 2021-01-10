package nitpicksy.paymentgateway.repository;

import nitpicksy.paymentgateway.enumeration.CompanyStatus;
import nitpicksy.paymentgateway.model.Company;
import nitpicksy.paymentgateway.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionPlan, Long> {

    SubscriptionPlan findByCompanyCommonNameAndCompanyPlanId(String companyCommonName, Long companyPlanId);

}
