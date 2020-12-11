package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.model.Membership;
import nitpicksy.literarysociety.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Membership findByUserId(Long id);

}
