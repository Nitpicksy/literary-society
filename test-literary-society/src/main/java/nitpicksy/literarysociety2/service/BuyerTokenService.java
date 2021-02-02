package nitpicksy.literarysociety2.service;

import nitpicksy.literarysociety2.model.BuyerToken;
import nitpicksy.literarysociety2.model.Transaction;

import java.security.NoSuchAlgorithmException;

public interface BuyerTokenService {

    String generateToken(Transaction transaction) throws NoSuchAlgorithmException;

    BuyerToken verifyToken(String token);

    void invalidateToken(Long tokenId);

    BuyerToken find(Long transactionId);
}
