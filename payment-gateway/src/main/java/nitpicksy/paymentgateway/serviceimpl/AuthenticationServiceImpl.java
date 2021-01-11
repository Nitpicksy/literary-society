package nitpicksy.paymentgateway.serviceimpl;

import nitpicksy.paymentgateway.dto.request.ChangePasswordDTO;
import nitpicksy.paymentgateway.dto.response.UserTokenState;
import nitpicksy.paymentgateway.enumeration.AdminStatus;
import nitpicksy.paymentgateway.exceptionHandler.BlockedUserException;
import nitpicksy.paymentgateway.exceptionHandler.InvalidUserDataException;
import nitpicksy.paymentgateway.model.Admin;
import nitpicksy.paymentgateway.model.Log;
import nitpicksy.paymentgateway.repository.AdminRepository;
import nitpicksy.paymentgateway.security.JwtAuthenticationRequest;
import nitpicksy.paymentgateway.security.TokenUtils;
import nitpicksy.paymentgateway.service.AuthenticationService;
import nitpicksy.paymentgateway.service.LogService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    public TokenUtils tokenUtils;

    private final AuthenticationManager authenticationManager;

    private final AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder;

    private final HttpServletRequest request;

    private final ChangePasswordAttemptService changePasswordAttemptService;

    private final LogService logService;

    @Override
    public UserTokenState login(JwtAuthenticationRequest authenticationRequest) {
        final Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Admin admin = (Admin) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(admin.getUsername(), admin.getRole().getName(),
                admin.getRole().getPermissions());
        String refreshJwt = tokenUtils.generateRefreshToken(admin.getUsername());
        int expiresIn = tokenUtils.getExpiredIn();

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "LGN", String.format("User %s successfully logged in", admin.getId())));
        return new UserTokenState(jwt, expiresIn, refreshJwt);
    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO)
            throws NullPointerException {
        String ip = getClientIP();
        if (changePasswordAttemptService.isBlocked(ip)) {
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CPW", String.format("User from %s is blocked.", getClientIP())));
            throw new BlockedUserException(
                    "You tried to log in too many times. Your account wil be blocked for the next 24 hours.",
                    HttpStatus.BAD_REQUEST);
        }

        Admin admin = adminRepository.findByUsername(changePasswordDTO.getUsername());

        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), admin.getPassword())) {
            changePasswordAttemptService.changePassFailed();
            throw new InvalidUserDataException("Invalid username or password. Please try again.", HttpStatus.BAD_REQUEST);
        }

        if (userIsNeverLoggedIn(changePasswordDTO.getUsername())) {
            admin.setStatus(AdminStatus.ACTIVE);
        }

        admin.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        admin.setLastPasswordResetDate(new Timestamp(DateTime.now().getMillis()));
        adminRepository.save(admin);

        changePasswordAttemptService.changePassSucceeded();
        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CPW", String.format("User %s successfully changed password", admin.getId())));
    }


    @Override
    public boolean userIsNeverLoggedIn(String username) {
        Admin admin = adminRepository.findByUsername(username);
        if (admin != null) {
            return admin.getStatus() == AdminStatus.NEVER_LOGGED_IN;
        }
        return false;
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }


    @Autowired
    public AuthenticationServiceImpl(TokenUtils tokenUtils, AuthenticationManager authenticationManager,
                                     AdminRepository adminRepository, PasswordEncoder passwordEncoder,
                                     HttpServletRequest request, ChangePasswordAttemptService changePasswordAttemptService,
                                     LogService logService) {
        this.tokenUtils = tokenUtils;
        this.authenticationManager = authenticationManager;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.request = request;
        this.changePasswordAttemptService = changePasswordAttemptService;
        this.logService = logService;
    }
}

