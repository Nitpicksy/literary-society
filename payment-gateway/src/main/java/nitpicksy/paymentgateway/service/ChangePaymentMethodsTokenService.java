package nitpicksy.paymentgateway.service;

import nitpicksy.paymentgateway.model.ChangePaymentMethodsToken;
import nitpicksy.paymentgateway.model.Company;

import java.security.NoSuchAlgorithmException;

public interface ChangePaymentMethodsTokenService {

    String generateToken(Company company) throws NoSuchAlgorithmException;

    ChangePaymentMethodsToken verifyToken(String token) throws NoSuchAlgorithmException;

    void invalidateToken(Long tokenId);

    ChangePaymentMethodsToken find(Long companyId);
}
