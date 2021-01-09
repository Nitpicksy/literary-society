package nitpicksy.paymentgateway.repository;

import nitpicksy.paymentgateway.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Transaction findTransactionByMerchantOrderIdAndPaymentId(String merchantOrderId, Long paymentId);

    List<Transaction> findByCompanyId(Long id);

    Transaction findOneById(Long id);
}
