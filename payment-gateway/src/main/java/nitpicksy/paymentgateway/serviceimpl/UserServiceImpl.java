package nitpicksy.paymentgateway.serviceimpl;


import nitpicksy.paymentgateway.dto.response.UserTokenState;
import nitpicksy.paymentgateway.exceptionHandler.InvalidUserDataException;
import nitpicksy.paymentgateway.model.Admin;
import nitpicksy.paymentgateway.model.Company;
import nitpicksy.paymentgateway.model.Log;
import nitpicksy.paymentgateway.model.Role;
import nitpicksy.paymentgateway.repository.AdminRepository;
import nitpicksy.paymentgateway.repository.CompanyRepository;
import nitpicksy.paymentgateway.security.TokenUtils;
import nitpicksy.paymentgateway.service.LogService;
import nitpicksy.paymentgateway.service.UserService;
import nitpicksy.paymentgateway.utils.IPAddressProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private AdminRepository adminRepository;

    private CompanyRepository companyRepository;

    private LoginAttemptService loginAttemptService;

    private Environment environment;

    public TokenUtils tokenUtils;

    private LogService logService;

    private IPAddressProvider ipAddressProvider;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // recommended work rounds is 12 (default is 10)
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String ip = loginAttemptService.getClientIP();
        if (loginAttemptService.isBlocked(ip)) {
            throw new RuntimeException("Blocked");
        }
        Admin admin = adminRepository.findByUsername(username);
        if(admin != null){
            return admin;
        }
        Company company = companyRepository.findByCommonName(username);
        if (company == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return company;
        }
    }

    @Override
    public UserDetails getAuthenticatedUser() {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            return null;
        }
        Admin admin = adminRepository.findByUsername(currentUser.getName());
        if(admin != null){
            return admin;
        }
        return companyRepository.findByCommonName(currentUser.getName());
    }

    @Override
    public UserTokenState refreshAuthenticationToken(HttpServletRequest request) {
        String refreshToken = tokenUtils.getToken(request);
        String username = this.tokenUtils.getUsernameFromToken(refreshToken);
        UserDetails userDetails = loadUserByUsername(username);
        Timestamp lastPasswordResetDate;
        Role role;
        if(userDetails instanceof Admin){
            Admin admin = (Admin) userDetails;
            lastPasswordResetDate = admin.getLastPasswordResetDate();
            role = admin.getRole();
        }else {
            Company company = (Company) userDetails;
            lastPasswordResetDate = company.getLastPasswordResetDate();
            role = company.getRole();
        }

        if (this.tokenUtils.canTokenBeRefreshed(refreshToken,lastPasswordResetDate)) {
            String newToken = tokenUtils.refreshToken(refreshToken, role);
            int expiresIn = tokenUtils.getExpiredIn();
            return new UserTokenState(newToken, expiresIn, refreshToken);
        } else {
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "REF", String.format("User from %s tried to refresh token manually", ipAddressProvider.get())));
            throw new InvalidUserDataException("Token can not be refreshed", HttpStatus.BAD_REQUEST);
        }
    }

    @Autowired
    public UserServiceImpl(AdminRepository adminRepository,CompanyRepository companyRepository, LoginAttemptService loginAttemptService,
                           Environment environment,TokenUtils tokenUtils,LogService logService,IPAddressProvider ipAddressProvider) {
        this.adminRepository = adminRepository;
        this.companyRepository=companyRepository;
        this.loginAttemptService = loginAttemptService;
        this.environment = environment;
        this.tokenUtils = tokenUtils;
        this.logService = logService;
        this.ipAddressProvider = ipAddressProvider;
    }
}
