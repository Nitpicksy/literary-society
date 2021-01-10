package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.enumeration.UserStatus;
import nitpicksy.literarysociety.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    Merchant findByName(String name);

    Merchant findFirstByOrderByIdAsc();

    List<Merchant> findByStatusIn(Collection<UserStatus> status);
}
