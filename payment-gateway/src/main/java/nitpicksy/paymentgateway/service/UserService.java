package nitpicksy.paymentgateway.service;

import nitpicksy.paymentgateway.dto.response.UserTokenState;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;


public interface UserService {

    UserDetails  getAuthenticatedUser();

    UserTokenState refreshAuthenticationToken(HttpServletRequest request);
}
