package nitpicksy.paymentgateway.repository;

import nitpicksy.paymentgateway.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    Merchant findByName(String name);
}
