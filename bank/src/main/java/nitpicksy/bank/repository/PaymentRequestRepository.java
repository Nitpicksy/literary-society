package nitpicksy.bank.repository;

import nitpicksy.bank.model.PaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRequestRepository extends JpaRepository<PaymentRequest, Long> {

    PaymentRequest findOneById(Long id);

    PaymentRequest findByMerchantOrderId(Long id);
}
