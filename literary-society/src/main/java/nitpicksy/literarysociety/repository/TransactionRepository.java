package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    Transaction findOneById(Long id);
}
