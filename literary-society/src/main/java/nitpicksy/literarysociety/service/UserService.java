package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.model.Role;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.model.UserTokenState;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

public interface UserService {

    User findByUsername(String username);

    User findByEmail(String email);

    Role findRoleByName(String name);

    void generateResetToken(String email) throws NoSuchAlgorithmException;

    UserTokenState refreshAuthenticationToken(HttpServletRequest request);

    User getAuthenticatedUser();
}
