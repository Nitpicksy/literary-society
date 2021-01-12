package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.model.JWTToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JWTTokenRepository extends JpaRepository<JWTToken, Long> {

    List<JWTToken> findAll();

}
