package nitpicksy.paypalservice.repository;

import nitpicksy.paypalservice.model.PaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRequestRepository extends JpaRepository<PaymentRequest, Long> {

    PaymentRequest findOneByPaymentId(String paymentId);

    PaymentRequest findOneById(Long id);
}
