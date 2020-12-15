package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.model.VerificationToken;

import java.security.NoSuchAlgorithmException;

public interface VerificationService {

    String generateToken(User user) throws NoSuchAlgorithmException;

    VerificationToken verifyToken(String token) throws NoSuchAlgorithmException;

    void invalidateToken(Long tokenId);
}
