package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.model.Image;
import nitpicksy.literarysociety.model.JWTToken;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JWTTokenRepository extends RefreshMethodRepository<JWTToken, Long> {

    List<JWTToken> findAll();

}
