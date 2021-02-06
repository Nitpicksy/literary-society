package nitpicksy.literarysociety2.repository;

import nitpicksy.literarysociety2.model.BuyerToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BuyerTokenRepository extends JpaRepository<BuyerToken, Long> {

    BuyerToken findByToken(String token);

    BuyerToken findByTokenAndExpiryDateTimeAfter(String token, LocalDateTime localDateTime);

    BuyerToken findByTransactionId(Long id);
}