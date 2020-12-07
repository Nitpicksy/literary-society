package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.model.User;

import java.security.NoSuchAlgorithmException;

public interface VerificationService {
    void generateToken(User user) throws NoSuchAlgorithmException;
}
