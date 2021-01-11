package nitpicksy.paymentgateway.repository;

import nitpicksy.paymentgateway.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    Merchant findByNameAndCompanyId(String name, Long id);

    Merchant findByIdAndCompanyId(Long id, Long companyId);

    List<Merchant> findByCompanyId(Long id);
}
