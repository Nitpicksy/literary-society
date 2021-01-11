package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.model.BuyerToken;
import nitpicksy.literarysociety.model.Transaction;
import nitpicksy.literarysociety.repository.BuyerTokenRepository;
import nitpicksy.literarysociety.service.BuyerTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
public class BuyerTokenServiceImpl implements BuyerTokenService {

    private BuyerTokenRepository buyerTokenRepository;

    @Override
    public String generateToken(Transaction transaction) throws NoSuchAlgorithmException {
        BuyerToken buyerToken = new BuyerToken(transaction);
        String nonHashedToken = buyerToken.getToken();
        buyerToken.setToken(getTokenHash(nonHashedToken));

        BuyerToken dbToken = buyerTokenRepository.findByToken(buyerToken.getToken());
        while (dbToken != null) {
            buyerToken = new BuyerToken(transaction);
            dbToken = buyerTokenRepository.findByToken(buyerToken.getToken());
        }
        buyerTokenRepository.save(buyerToken);

        return nonHashedToken;
    }

    @Override
    public BuyerToken verifyToken(String token) {
        return buyerTokenRepository.findByTokenAndExpiryDateTimeAfter(token, LocalDateTime.now());
    }

    @Override
    public void invalidateToken(Long tokenId) {
        buyerTokenRepository.deleteById(tokenId);
    }

    @Override
    public BuyerToken find(Long transactionId) {
        return buyerTokenRepository.findByTransactionId(transactionId);
    }

    private String getTokenHash(String token) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.reset();
        digest.update(token.getBytes());
        return String.format("%040x", new BigInteger(1, digest.digest()));
    }


    @Autowired
    public BuyerTokenServiceImpl( BuyerTokenRepository buyerTokenRepository) {
        this.buyerTokenRepository = buyerTokenRepository;
    }
}