package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.model.BuyerToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BuyerTokenRepository extends JpaRepository<BuyerToken, Long> {

    BuyerToken findByToken(String token);

    BuyerToken findByTokenAndExpiryDateTimeAfter(String token, LocalDateTime localDateTime);

    BuyerToken findByTransactionId(Long id);
}