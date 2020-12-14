package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.model.Membership;
import nitpicksy.literarysociety.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant,Long> {

    Merchant findByName(String name);
}
