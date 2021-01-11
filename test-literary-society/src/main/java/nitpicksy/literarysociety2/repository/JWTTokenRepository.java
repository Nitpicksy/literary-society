package nitpicksy.literarysociety2.repository;

import nitpicksy.literarysociety2.model.JWTToken;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JWTTokenRepository extends RefreshMethodRepository<JWTToken, Long> {

    List<JWTToken> findAll();

}
