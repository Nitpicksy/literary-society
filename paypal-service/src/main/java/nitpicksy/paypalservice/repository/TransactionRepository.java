package nitpicksy.paypalservice.repository;

import nitpicksy.paypalservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    Transaction findByPaymentId(Long id);
}
