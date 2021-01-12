package nitpicksy.paymentgateway.repository;

import nitpicksy.paymentgateway.model.ChangePaymentMethodsToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ChangePaymentMethodsTokenRepository extends JpaRepository<ChangePaymentMethodsToken, Long> {

    ChangePaymentMethodsToken findByToken(String token);

    ChangePaymentMethodsToken findByTokenAndExpiryDateTimeAfter(String token, LocalDateTime localDateTime);

    ChangePaymentMethodsToken findByCompanyId(Long id);
}
