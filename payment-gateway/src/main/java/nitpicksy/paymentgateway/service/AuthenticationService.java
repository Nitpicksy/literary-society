package nitpicksy.paymentgateway.service;

import nitpicksy.paymentgateway.dto.request.ChangePasswordDTO;
import nitpicksy.paymentgateway.dto.response.UserTokenState;
import nitpicksy.paymentgateway.security.JwtAuthenticationRequest;

import java.security.NoSuchAlgorithmException;

public interface AuthenticationService {

    UserTokenState login(JwtAuthenticationRequest authenticationRequest);

    void changePassword(ChangePasswordDTO changePasswordDTO) throws NullPointerException, NoSuchAlgorithmException;

    boolean userIsNeverLoggedIn(String username);

}
