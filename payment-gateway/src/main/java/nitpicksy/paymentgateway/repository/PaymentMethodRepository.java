package nitpicksy.paymentgateway.repository;

import nitpicksy.paymentgateway.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    PaymentMethod findByCommonName(String commonName);
}
