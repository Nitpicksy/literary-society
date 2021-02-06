package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.dto.request.ChangePasswordDTO;
import nitpicksy.literarysociety.dto.request.ResetPasswordDTO;
import nitpicksy.literarysociety.model.UserTokenState;
import nitpicksy.literarysociety.security.JwtAuthenticationRequest;

import java.security.NoSuchAlgorithmException;

public interface AuthenticationService {

    UserTokenState login(JwtAuthenticationRequest authenticationRequest);

    void changePassword(ChangePasswordDTO changePasswordDTO) throws NullPointerException, NoSuchAlgorithmException;

    void resetPassword(String token, ResetPasswordDTO resetPasswordDTO) throws NoSuchAlgorithmException;

    boolean userIsNeverLoggedIn(String username);

    void activateAccount(String hash) throws NoSuchAlgorithmException;

    void deactivateInactiveUsers();
}
