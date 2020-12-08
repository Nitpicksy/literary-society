package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.model.VerificationToken;
import nitpicksy.literarysociety.repository.VerificationTokenRepository;
import nitpicksy.literarysociety.service.EmailNotificationService;
import nitpicksy.literarysociety.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class VerificationServiceImpl implements VerificationService {

    private VerificationTokenRepository verificationTokenRepository;

    private Environment environment;

    private EmailNotificationService emailNotificationService;

    @Override
    public void generateToken(User user) throws NoSuchAlgorithmException {
        VerificationToken verificationToken = new VerificationToken(user);
        String nonHashedToken = verificationToken.getToken();
        verificationToken.setToken(getTokenHash(nonHashedToken));

        VerificationToken dbToken = verificationTokenRepository.findByToken(verificationToken.getToken());
        while (dbToken != null) {
            verificationToken = new VerificationToken(user);
            dbToken = verificationTokenRepository.findByToken(verificationToken.getToken());
        }
        verificationTokenRepository.save(verificationToken);

        composeAndSendEmailToActivate(user.getEmail(), nonHashedToken);
    }

    private String getTokenHash(String token) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.reset();
        digest.update(token.getBytes());
        return String.format("%040x", new BigInteger(1, digest.digest()));
    }

    private void composeAndSendEmailToActivate(String recipientEmail, String token) {
        String subject = "Activate your account";
        StringBuilder sb = new StringBuilder();
        sb.append("You have successfully registered to the Literary Society website.");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("To activate your account click the following link:");
        sb.append(System.lineSeparator());
        sb.append(getLocalhostURL());
        sb.append("activate-account?t=" + token);
        String text = sb.toString();

        emailNotificationService.sendEmail(recipientEmail, subject, text);
    }

    private String getLocalhostURL() {
        return environment.getProperty("LOCALHOST_URL");

    }

    @Autowired
    public VerificationServiceImpl(VerificationTokenRepository verificationTokenRepository, Environment environment, EmailNotificationService emailNotificationService) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.environment = environment;
        this.emailNotificationService = emailNotificationService;
    }
}
