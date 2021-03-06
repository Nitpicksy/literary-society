package nitpicksy.literarysociety2.repository;

import nitpicksy.literarysociety2.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Membership findByUserIdAndExpirationDateGreaterThanEqual(Long id, LocalDate localDate);

    Membership findByUserIdAndExpirationDateIsNullAndIsSubscribedIsTrue(Long id);

}
