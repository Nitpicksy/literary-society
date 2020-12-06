package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.enumeration.UserStatus;
import nitpicksy.literarysociety.exceptionHandler.InvalidUserDataException;
import nitpicksy.literarysociety.model.Log;
import nitpicksy.literarysociety.model.ResetToken;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.model.UserTokenState;
import nitpicksy.literarysociety.repository.ResetTokenRepository;
import nitpicksy.literarysociety.repository.UserRepository;
import nitpicksy.literarysociety.security.TokenUtils;
import nitpicksy.literarysociety.service.EmailNotificationService;
import nitpicksy.literarysociety.service.LogService;
import nitpicksy.literarysociety.service.UserService;
import nitpicksy.literarysociety.utils.IPAddressProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private UserRepository userRepository;

    private LoginAttemptService loginAttemptService;

    private LogService logService;

    private IPAddressProvider ipAddressProvider;

    private Environment environment;

    private EmailNotificationService emailNotificationService;

    private ResetTokenRepository resetTokenRepository;

    public TokenUtils tokenUtils;

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
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with email '%s'.", username));
        } else {
            return user;
        }
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void generateResetToken(String username) throws NoSuchAlgorithmException {
        User user = findByUsername(username);
        if (user == null) {
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "RPW", String.format("User from %s tried to reset password for an unregistered user", ipAddressProvider.get())));
            return;
        }

        if(user.getStatus() != UserStatus.ACTIVE){
            composeAndSendEmail(user.getEmail());
            return;
        }

        ResetToken resetToken = new ResetToken(user);
        String nonHashedToken = resetToken.getToken();
        resetToken.setToken(getTokenHash(nonHashedToken));

        ResetToken dbToken = resetTokenRepository.findByToken(resetToken.getToken());
        while (dbToken != null) {
            resetToken = new ResetToken(user);
            dbToken = resetTokenRepository.findByToken(resetToken.getToken());
        }
        resetTokenRepository.save(resetToken);

        composeAndSendResetLink(user.getEmail(), nonHashedToken);
    }

    @Override
    public UserTokenState refreshAuthenticationToken(HttpServletRequest request) {
        String refreshToken = tokenUtils.getToken(request);
        String username = this.tokenUtils.getUsernameFromToken(refreshToken);
        User user = (User) loadUserByUsername(username);

        if (this.tokenUtils.canTokenBeRefreshed(refreshToken, user.getLastPasswordResetDate())) {
            String newToken = tokenUtils.refreshToken(refreshToken, user);
            int expiresIn = tokenUtils.getExpiredIn();
            return new UserTokenState(newToken, expiresIn, refreshToken);
        } else {
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "REF", String.format("User from %s tried to refresh token manually", ipAddressProvider.get())));
            throw new InvalidUserDataException("Token can not be refreshed", HttpStatus.BAD_REQUEST);
        }
    }

    private void composeAndSendEmail(String recipientEmail) {
        String subject = "Reset your password";
        StringBuilder sb = new StringBuilder();
        sb.append("You got this email because you requested a password reset.");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("Your account is not active, therefore your password cannot be reset. ");
        sb.append(
                "To activate it, use the previously received activation link or change the previously received generic password.");
        String text = sb.toString();

        emailNotificationService.sendEmail(recipientEmail, subject, text);
    }

    private void composeAndSendResetLink(String recipientEmail, String token) {
        String subject = "Reset your password";
        StringBuilder sb = new StringBuilder();
        sb.append("You got this email because you requested a password reset.");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("To reset your password click the following link:");
        sb.append(System.lineSeparator());
        sb.append(getLocalhostURL());
        sb.append("reset-password?t=");
        sb.append(token);
        String text = sb.toString();

        emailNotificationService.sendEmail(recipientEmail, subject, text);
    }

    private String getLocalhostURL() {
        return environment.getProperty("LOCALHOST_URL");
    }

    private String getTokenHash(String token) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.reset();
        digest.update(token.getBytes());
        return String.format("%040x", new BigInteger(1, digest.digest()));
    }



    @Autowired
    public UserServiceImpl(UserRepository userRepository, LoginAttemptService loginAttemptService, LogService logService,
                           IPAddressProvider ipAddressProvider, Environment environment,EmailNotificationService emailNotificationService,
                           ResetTokenRepository resetTokenRepository) {
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
        this.logService = logService;
        this.ipAddressProvider = ipAddressProvider;
        this.environment = environment;
        this.emailNotificationService = emailNotificationService;
        this.resetTokenRepository = resetTokenRepository;
    }
}