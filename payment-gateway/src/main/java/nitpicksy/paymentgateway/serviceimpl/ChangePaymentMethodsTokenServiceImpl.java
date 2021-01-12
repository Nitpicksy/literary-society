package nitpicksy.paymentgateway.serviceimpl;

import nitpicksy.paymentgateway.model.ChangePaymentMethodsToken;
import nitpicksy.paymentgateway.model.Company;
import nitpicksy.paymentgateway.repository.ChangePaymentMethodsTokenRepository;
import nitpicksy.paymentgateway.service.ChangePaymentMethodsTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
public class ChangePaymentMethodsTokenServiceImpl implements ChangePaymentMethodsTokenService {

    private ChangePaymentMethodsTokenRepository changePaymentMethodsTokenRepository;

    @Override
    public String generateToken(Company company) throws NoSuchAlgorithmException {
        ChangePaymentMethodsToken changePaymentMethodsToken = new ChangePaymentMethodsToken(company);
        String nonHashedToken = changePaymentMethodsToken.getToken();
        changePaymentMethodsToken.setToken(getTokenHash(nonHashedToken));

        ChangePaymentMethodsToken dbToken = changePaymentMethodsTokenRepository.findByToken(changePaymentMethodsToken.getToken());
        while (dbToken != null) {
            changePaymentMethodsToken = new ChangePaymentMethodsToken(company);
            nonHashedToken = changePaymentMethodsToken.getToken();
            changePaymentMethodsToken.setToken(getTokenHash(nonHashedToken));
            dbToken = changePaymentMethodsTokenRepository.findByToken(changePaymentMethodsToken.getToken());
        }
        changePaymentMethodsTokenRepository.save(changePaymentMethodsToken);

        return nonHashedToken;
    }

    @Override
    public ChangePaymentMethodsToken verifyToken(String token) throws NoSuchAlgorithmException {
        return changePaymentMethodsTokenRepository.findByTokenAndExpiryDateTimeAfter(getTokenHash(token), LocalDateTime.now());
    }

    @Override
    public void invalidateToken(Long tokenId) {
        changePaymentMethodsTokenRepository.deleteById(tokenId);
    }

    @Override
    public ChangePaymentMethodsToken find(Long companyId) {
        return changePaymentMethodsTokenRepository.findByCompanyId(companyId);
    }

    private String getTokenHash(String token) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.reset();
        digest.update(token.getBytes());
        return String.format("%040x", new BigInteger(1, digest.digest()));
    }

    @Autowired
    public ChangePaymentMethodsTokenServiceImpl(ChangePaymentMethodsTokenRepository changePaymentMethodsTokenRepository) {
        this.changePaymentMethodsTokenRepository = changePaymentMethodsTokenRepository;
    }
}
