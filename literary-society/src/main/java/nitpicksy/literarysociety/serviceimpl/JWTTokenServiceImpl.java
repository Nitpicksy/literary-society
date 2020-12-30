package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.model.JWTToken;
import nitpicksy.literarysociety.repository.JWTTokenRepository;
import nitpicksy.literarysociety.service.JWTTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JWTTokenServiceImpl implements JWTTokenService {

    private JWTTokenRepository jwtTokenRepository;

    @Override
    public String getToken() {
        List<JWTToken> jwtTokenList = jwtTokenRepository.findAll();
        if (!jwtTokenList.isEmpty()) {
            return jwtTokenList.get(0).getToken();
        }
        return null;
    }

    @Autowired
    public JWTTokenServiceImpl(JWTTokenRepository jwtTokenRepository) {
        this.jwtTokenRepository = jwtTokenRepository;
    }
}
