package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.model.BuyerToken;
import nitpicksy.literarysociety.model.Transaction;

import java.security.NoSuchAlgorithmException;

public interface BuyerTokenService {

    String generateToken(Transaction transaction) throws NoSuchAlgorithmException;

    BuyerToken verifyToken(String token);

    void invalidateToken(Long tokenId);

    BuyerToken find(Long transactionId);
}
