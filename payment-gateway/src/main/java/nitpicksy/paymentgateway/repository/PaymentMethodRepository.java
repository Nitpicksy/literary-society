package nitpicksy.paymentgateway.repository;

import nitpicksy.paymentgateway.enumeration.PaymentMethodStatus;
import nitpicksy.paymentgateway.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    PaymentMethod findByName(String name);

    PaymentMethod findByCommonName(String commonName);

    List<PaymentMethod> findByStatusNot(PaymentMethodStatus status);
}
