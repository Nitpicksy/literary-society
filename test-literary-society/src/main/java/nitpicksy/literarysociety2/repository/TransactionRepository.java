package nitpicksy.literarysociety2.repository;

import nitpicksy.literarysociety2.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    Transaction findOneById(Long id);
}
