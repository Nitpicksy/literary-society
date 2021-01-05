package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    
    Membership findByUserIdAndExpirationDateGreaterThanEqual(Long id, LocalDate localDate);

}
