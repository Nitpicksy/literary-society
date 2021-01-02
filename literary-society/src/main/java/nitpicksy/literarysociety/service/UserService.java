package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.model.Merchant;
import nitpicksy.literarysociety.model.Role;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.model.UserTokenState;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserService {

    User findByUsername(String username);

    User findByEmail(String email);

    User findById(Long id);

    Role findRoleByName(String name);

    List<User> findAllWithRole(String roleName);

    void generateResetToken(String email) throws NoSuchAlgorithmException;

    UserTokenState refreshAuthenticationToken(HttpServletRequest request);

    User getAuthenticatedUser();

    Merchant getAuthenticatedMerchant();

    User signUp(User user) throws NoSuchAlgorithmException;
}
