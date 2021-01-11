package nitpicksy.bank.service;

import nitpicksy.bank.model.Account;
import nitpicksy.bank.model.CreditCard;

import java.security.NoSuchAlgorithmException;

public interface CreditCardService {

    CreditCard checkCreditCardDate(String pan, String cardHolderName, String expirationDate, String securityCode) throws NoSuchAlgorithmException;

    CreditCard checkCreditCardDateHashedValues(String pan, String cardHolderName, String expirationDate, String securityCode);

    boolean isClientOfThisBank(String pan);

    CreditCard create(Account account) throws NoSuchAlgorithmException;
}
