package nitpicksy.bank.repository;

import nitpicksy.bank.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

    CreditCard findByPanAndCardHolderName(String pan,String cardHolderName);
}