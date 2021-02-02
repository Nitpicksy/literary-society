package nitpicksy.literarysociety2.service;

import nitpicksy.literarysociety2.dto.request.ChangePasswordDTO;
import nitpicksy.literarysociety2.dto.request.ResetPasswordDTO;
import nitpicksy.literarysociety2.model.UserTokenState;
import nitpicksy.literarysociety2.security.JwtAuthenticationRequest;

import java.security.NoSuchAlgorithmException;

public interface AuthenticationService {

    UserTokenState login(JwtAuthenticationRequest authenticationRequest);

    void changePassword(ChangePasswordDTO changePasswordDTO) throws NullPointerException, NoSuchAlgorithmException;

    void resetPassword(String token, ResetPasswordDTO resetPasswordDTO) throws NoSuchAlgorithmException;

    boolean userIsNeverLoggedIn(String username);

    void activateAccount(String hash) throws NoSuchAlgorithmException;
}
