package nitpicksy.paymentgateway.repository;

import nitpicksy.paymentgateway.enumeration.PaymentMethodStatus;
import nitpicksy.paymentgateway.model.Company;
import nitpicksy.paymentgateway.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    PaymentMethod findByName(String name);

    PaymentMethod findByCommonName(String commonName);

    PaymentMethod findByCertificateName(String certificateName);

    List<PaymentMethod> findByStatusNot(PaymentMethodStatus status);

    List<PaymentMethod> findByStatus(PaymentMethodStatus status);

    List<PaymentMethod> findByIdIn(List<Long> ids);

    List<PaymentMethod> findByIdInAndIdNotIn(Set<Long> ids, Set<Long> idNot);
}
