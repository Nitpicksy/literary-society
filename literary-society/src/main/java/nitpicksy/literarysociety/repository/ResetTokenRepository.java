package nitpicksy.literarysociety.repository;
import nitpicksy.literarysociety.model.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {

    ResetToken findByToken(String token);

    ResetToken findByTokenAndExpiryDateTimeAfter(String token, LocalDateTime localDateTime);

}
