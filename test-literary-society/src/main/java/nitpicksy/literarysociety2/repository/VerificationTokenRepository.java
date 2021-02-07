package nitpicksy.literarysociety2.repository;

import nitpicksy.literarysociety2.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByTokenAndExpiryDateTimeAfter(String token, LocalDateTime localDateTime);
    
}
