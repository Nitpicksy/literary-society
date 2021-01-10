package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.enumeration.UserStatus;
import nitpicksy.literarysociety.model.Merchant;
import nitpicksy.literarysociety.model.Reader;
import nitpicksy.literarysociety.model.Role;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.model.UserTokenState;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;

public interface UserService {

    User findByUsername(String username);

    User findByEmail(String email);

    User findById(Long id);

    Role findRoleByName(String name);

    List<User> findAllWithRoleAndStatus(String roleName, UserStatus status);

    void generateResetToken(String email) throws NoSuchAlgorithmException;

    UserTokenState refreshAuthenticationToken(HttpServletRequest request);

    User getAuthenticatedUser();

    Reader getAuthenticatedReader();

    Merchant getAuthenticatedMerchant();

    User signUp(User user) throws NoSuchAlgorithmException;

    List<User> findByRoleNameAndStatusInOrRoleNameAndStatusIn(String roleName1, Collection<UserStatus> status1,
                                                              String roleName2, Collection<UserStatus> status2);

    User changeUserStatus(Long id, String status);

    List<User> findByIds(List<Long> ids);
}
