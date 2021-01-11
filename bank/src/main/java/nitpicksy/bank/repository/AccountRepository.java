package nitpicksy.bank.repository;

import nitpicksy.bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByCreditCardsId(Long id);

    Account findByEmail(String email);
}
