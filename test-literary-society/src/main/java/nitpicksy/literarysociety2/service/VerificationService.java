package nitpicksy.literarysociety2.service;

import nitpicksy.literarysociety2.model.User;
import nitpicksy.literarysociety2.model.VerificationToken;

import java.security.NoSuchAlgorithmException;

public interface VerificationService {

    String generateToken(User user) throws NoSuchAlgorithmException;

    VerificationToken verifyToken(String token) throws NoSuchAlgorithmException;

    void invalidateToken(Long tokenId);
}
