package nitpicksy.bank.service;

import nitpicksy.bank.model.CreditCard;

import java.security.NoSuchAlgorithmException;

public interface CreditCardService {

    CreditCard checkCreditCardDate(String pan, String cardHolderName, String expirationDate, String securityCode) throws NoSuchAlgorithmException;
}
