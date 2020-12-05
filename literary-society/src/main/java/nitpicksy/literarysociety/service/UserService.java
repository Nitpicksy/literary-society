package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.model.UserTokenState;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

public interface UserService {

    User findByUsername(String username);

    void generateResetToken(String email) throws NoSuchAlgorithmException;

    UserTokenState refreshAuthenticationToken(HttpServletRequest request);
}
