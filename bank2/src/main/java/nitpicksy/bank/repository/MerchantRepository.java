package nitpicksy.bank.repository;

import nitpicksy.bank.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    Merchant findByMerchantId(String merchantId);
}
