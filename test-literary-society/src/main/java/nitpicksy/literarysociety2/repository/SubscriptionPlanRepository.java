package nitpicksy.literarysociety2.repository;

import nitpicksy.literarysociety2.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {

    SubscriptionPlan findOneByPlanNameContainingIgnoringCase(String nameSubstring);

    SubscriptionPlan findOneById(Long id);

}
