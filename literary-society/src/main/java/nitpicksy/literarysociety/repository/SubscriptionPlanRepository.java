package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.model.Membership;
import nitpicksy.literarysociety.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {

    SubscriptionPlan findOneByPlanNameContainingIgnoringCase(String nameSubstring);

    SubscriptionPlan findOneById(Long id);

}
