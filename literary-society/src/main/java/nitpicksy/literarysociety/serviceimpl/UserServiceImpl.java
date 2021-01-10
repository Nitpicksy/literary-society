package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.enumeration.UserStatus;
import nitpicksy.literarysociety.exceptionHandler.InvalidDataException;
import nitpicksy.literarysociety.exceptionHandler.InvalidUserDataException;
import nitpicksy.literarysociety.model.*;
import nitpicksy.literarysociety.repository.ResetTokenRepository;
import nitpicksy.literarysociety.repository.RoleRepository;
import nitpicksy.literarysociety.repository.UserRepository;
import nitpicksy.literarysociety.security.TokenUtils;
import nitpicksy.literarysociety.service.EmailNotificationService;
import nitpicksy.literarysociety.service.LogService;
import nitpicksy.literarysociety.service.UserService;
import nitpicksy.literarysociety.service.VerificationService;
import nitpicksy.literarysociety.utils.IPAddressProvider;
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
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    public RoleRepository roleRepository;

    private VerificationService verificationService;


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
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return user;
        }
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findOneById(id);
    }

    @Override
    public Role findRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public List<User> findAllWithRoleAndStatus(String roleName, UserStatus status) {
        return userRepository.findByRoleNameAndStatus(roleName, status);
    }

    @Override
    public void generateResetToken(String username) throws NoSuchAlgorithmException {
        User user = findByUsername(username);
        if (user == null) {
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "RPW", String.format("User from %s tried to reset password for an unregistered user", ipAddressProvider.get())));
            return;
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
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

    @Override
    public User getAuthenticatedUser() {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            return null;
        }
        return userRepository.findByUsername(currentUser.getName());
    }

    @Override
    public Reader getAuthenticatedReader() {
        User user = getAuthenticatedUser();
        if(user instanceof Reader){
            return (Reader)user;
        }
        return null;
    }

    @Override
    public Merchant getAuthenticatedMerchant() {
        User user = getAuthenticatedUser();
        if (user instanceof Merchant) {
            return (Merchant) user;
        }
        return null;
    }

    @Override
    public User signUp(User user) throws NoSuchAlgorithmException {
        if (findByUsername(user.getUsername()) != null) {
            throw new InvalidDataException("User with same username already exist", HttpStatus.BAD_REQUEST);
        }

        if (findByEmail(user.getEmail()) != null) {
            throw new InvalidDataException("User with same email already exist", HttpStatus.BAD_REQUEST);
        }
        user.setStatus(UserStatus.NON_VERIFIED);
        User savedReader = userRepository.save(user);
        String nonHashedToken = verificationService.generateToken(savedReader);
        composeEmailToActivate(nonHashedToken, user.getEmail());
        return savedReader;
    }

    @Override
    public List<User> findByRoleNameAndStatusInOrRoleNameAndStatusIn(String roleName1, Collection<UserStatus> status1,
                                                                     String roleName2, Collection<UserStatus> status2) {
        return userRepository.findByRoleNameAndStatusInOrRoleNameAndStatusIn(roleName1, status1, roleName2, status2);
    }

    @Override
    public User changeUserStatus(Long id, String status) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (status.equals("approve")) {

                user.setStatus(UserStatus.ACTIVE);
                composeAndSendEmailApprovedRequest(user.getEmail());
            } else {
                user.setStatus(UserStatus.REJECTED);
                composeAndSendRejectionEmail(user.getEmail());
            }
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public List<User> findByIds(List<Long> ids) {
        return userRepository.findByIdIn(ids);
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

    private void composeEmailToActivate(String token, String email) {
        StringBuilder sb = new StringBuilder();
        sb.append("You have successfully registered to the Literary Society website.");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("To activate your account click the following link:");
        sb.append(System.lineSeparator());
        sb.append(getLocalhostURL());
        sb.append(String.format("activate-account?t=%s", token));
        sb.append(System.lineSeparator());
        sb.append("When you activate your account you will be notified when the admins approves your request.");
        emailNotificationService.sendEmail(email, "Activate account", sb.toString());
    }

    private void composeAndSendRejectionEmail(String recipientEmail) {
        String subject = "Request to register rejected";
        StringBuilder sb = new StringBuilder();
        sb.append("Your request to register is rejected by a Literary Society administrator.");
        sb.append(System.lineSeparator());
        String text = sb.toString();
        emailNotificationService.sendEmail(recipientEmail, subject, text);
    }

    private void composeAndSendEmailApprovedRequest(String recipientEmail) {
        String subject = "Request to register accepted";
        StringBuilder sb = new StringBuilder();
        sb.append("Your request to register is accepted by a Literary Society administrator.");
        sb.append(System.lineSeparator());
        String text = sb.toString();
        emailNotificationService.sendEmail(recipientEmail, subject, text);
    }


    @Autowired
    public UserServiceImpl(UserRepository userRepository, LoginAttemptService loginAttemptService,
                           LogService logService, IPAddressProvider ipAddressProvider, Environment environment,
                           EmailNotificationService emailNotificationService, ResetTokenRepository resetTokenRepository,
                           TokenUtils tokenUtils, RoleRepository roleRepository, VerificationService verificationService) {
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
        this.logService = logService;
        this.ipAddressProvider = ipAddressProvider;
        this.environment = environment;
        this.emailNotificationService = emailNotificationService;
        this.resetTokenRepository = resetTokenRepository;
        this.tokenUtils = tokenUtils;
        this.roleRepository = roleRepository;
        this.verificationService = verificationService;
    }
}
