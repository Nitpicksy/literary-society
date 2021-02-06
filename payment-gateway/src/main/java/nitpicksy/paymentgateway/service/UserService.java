package nitpicksy.paymentgateway.service;

import nitpicksy.paymentgateway.dto.request.JWTRequestDTO;
import nitpicksy.paymentgateway.dto.response.UserTokenState;
import nitpicksy.paymentgateway.model.Company;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;


public interface UserService {

    UserDetails  getAuthenticatedUser();

    Company getAuthenticatedCompany();

    UserTokenState refreshAuthenticationToken(HttpServletRequest request);

    JWTRequestDTO companyRefreshAuthenticationToken(HttpServletRequest request);
}
