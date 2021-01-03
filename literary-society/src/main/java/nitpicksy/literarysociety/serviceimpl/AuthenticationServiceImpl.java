package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.constants.RoleConstants;
import nitpicksy.literarysociety.dto.request.ChangePasswordDTO;
import nitpicksy.literarysociety.dto.request.ResetPasswordDTO;
import nitpicksy.literarysociety.enumeration.UserStatus;
import nitpicksy.literarysociety.exceptionHandler.BlockedUserException;
import nitpicksy.literarysociety.exceptionHandler.InvalidTokenException;
import nitpicksy.literarysociety.exceptionHandler.InvalidUserDataException;
import nitpicksy.literarysociety.model.*;
import nitpicksy.literarysociety.repository.ResetTokenRepository;
import nitpicksy.literarysociety.repository.UserRepository;
import nitpicksy.literarysociety.security.JwtAuthenticationRequest;
import nitpicksy.literarysociety.security.TokenUtils;
import nitpicksy.literarysociety.service.AuthenticationService;
import nitpicksy.literarysociety.service.LogService;
import nitpicksy.literarysociety.service.VerificationService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    public TokenUtils tokenUtils;

    private AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final VerificationService verificationService;

    private final ResetTokenRepository resetTokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final RestTemplate restTemplate;

    private final HttpServletRequest request;

    private ChangePasswordAttemptService changePasswordAttemptService;

    private final LogService logService;

    @Override
    public UserTokenState login(JwtAuthenticationRequest authenticationRequest) {
        final Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user.getUsername(), user.getRole().getName(),
                user.getRole().getPermissions());
        String refreshJwt = tokenUtils.generateRefreshToken(user.getUsername());
        int expiresIn = tokenUtils.getExpiredIn();

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "LGN", String.format("User %s successfully logged in", user.getId())));
        return new UserTokenState(jwt, expiresIn, refreshJwt);
    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO)
            throws NullPointerException, NoSuchAlgorithmException {
        String ip = getClientIP();
        if (changePasswordAttemptService.isBlocked(ip)) {
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CPW", String.format("User from %s is blocked.", getClientIP())));
            throw new BlockedUserException(
                    "You tried to log in too many times. Your account wil be blocked for the next 24 hours.",
                    HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByUsername(changePasswordDTO.getUsername());

        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            changePasswordAttemptService.changePassFailed();
            throw new InvalidUserDataException("Invalid username or password. Please try again.", HttpStatus.BAD_REQUEST);
        }

        if (userIsNeverLoggedIn(changePasswordDTO.getUsername())) {
            user.setStatus(UserStatus.ACTIVE);
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        user.setLastPasswordResetDate(new Timestamp(DateTime.now().getMillis()));
        userRepository.save(user);

        changePasswordAttemptService.changePassSucceeded();
        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CPW", String.format("User %s successfully changed password", user.getId())));
    }


    @Override
    public void resetPassword(String token, ResetPasswordDTO resetPasswordDTO) throws NoSuchAlgorithmException {
        ResetToken resetToken = resetTokenRepository.findByTokenAndExpiryDateTimeAfter(getTokenHash(token),
                LocalDateTime.now());
        if (resetToken == null) {
            throw new InvalidTokenException("This reset token is invalid or expired.", HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByUsername(resetToken.getUser().getUsername());
        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
        user.setLastPasswordResetDate(new Timestamp(DateTime.now().getMillis()));
        userRepository.save(user);

        resetTokenRepository.deleteById(resetToken.getId());
    }

    @Override
    public void activateAccount(String hash) throws NoSuchAlgorithmException {
        VerificationToken token = verificationService.verifyToken(hash);

        if (token == null) {
            throw new InvalidTokenException("This activation link is invalid or expired.", HttpStatus.BAD_REQUEST);
        }

        User user = token.getUser();
        user.setEnabled(true);

        if (user.getRole().getName().equals(RoleConstants.ROLE_WRITER)) {
            user.setStatus(UserStatus.WAITING_APPROVAL);
        }else if (user.getRole().getName().equals(RoleConstants.ROLE_EDITOR)) {
            user.setStatus(UserStatus.WAITING_APPROVAL);
        }else if (user.getRole().getName().equals(RoleConstants.ROLE_LECTURER)) {
            user.setStatus(UserStatus.WAITING_APPROVAL);
        }else {
            user.setStatus(UserStatus.ACTIVE);
        }

        verificationService.invalidateToken(token.getId());
        userRepository.save(user);
    }

    @Override
    public boolean userIsNeverLoggedIn(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        return user.getStatus() == UserStatus.NEVER_LOGGED_IN;
    }


    private String getTokenHash(String token) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.reset();
        digest.update(token.getBytes());
        return String.format("%040x", new BigInteger(1, digest.digest()));
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
                                     UserRepository userRepository, PasswordEncoder passwordEncoder, RestTemplateBuilder restTemplateBuilder,
                                     HttpServletRequest request, ChangePasswordAttemptService changePasswordAttemptService,
                                     ResetTokenRepository resetTokenRepository, LogService logService, VerificationService verificationService) {
        this.tokenUtils = tokenUtils;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplateBuilder.build();
        this.request = request;
        this.changePasswordAttemptService = changePasswordAttemptService;
        this.resetTokenRepository = resetTokenRepository;
        this.logService = logService;
        this.verificationService = verificationService;
    }
}
