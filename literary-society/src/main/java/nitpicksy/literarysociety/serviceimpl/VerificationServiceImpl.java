package nitpicksy.literarysociety.serviceimpl;


import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.model.VerificationToken;
import nitpicksy.literarysociety.repository.VerificationTokenRepository;
import nitpicksy.literarysociety.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
public class VerificationServiceImpl implements VerificationService {

    private VerificationTokenRepository verificationTokenRepository;

    /**
     * @param user
     * @return nonHashedToken to be sent by email
     * @throws NoSuchAlgorithmException
     */
    @Override
    public String generateToken(User user) throws NoSuchAlgorithmException {
        VerificationToken verificationToken = new VerificationToken(user);
        String nonHashedToken = verificationToken.getToken();
        verificationToken.setToken(getTokenHash(nonHashedToken));

        VerificationToken dbToken = verificationTokenRepository.findByToken(verificationToken.getToken());
        while (dbToken != null) {
            verificationToken = new VerificationToken(user);
            nonHashedToken = verificationToken.getToken();
            verificationToken.setToken(getTokenHash(nonHashedToken));
            dbToken = verificationTokenRepository.findByToken(verificationToken.getToken());
        }
        verificationTokenRepository.save(verificationToken);

        return nonHashedToken;
    }

    @Override
    public VerificationToken verifyToken(String token) throws NoSuchAlgorithmException {
        return verificationTokenRepository
                .findByTokenAndExpiryDateTimeAfter(getTokenHash(token), LocalDateTime.now());
    }

    @Override
    public void invalidateToken(Long tokenId) {
        verificationTokenRepository.deleteById(tokenId);
    }

    private String getTokenHash(String token) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.reset();
        digest.update(token.getBytes());
        return String.format("%040x", new BigInteger(1, digest.digest()));
    }


    @Autowired
    public VerificationServiceImpl(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }
}
