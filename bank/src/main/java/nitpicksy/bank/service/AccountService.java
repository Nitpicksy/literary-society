package nitpicksy.bank.service;

import nitpicksy.bank.model.Account;
import nitpicksy.bank.model.CreditCard;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface AccountService {

    boolean hasEnoughMoney(Double amount, CreditCard creditCard);

    List<Account> findAll();

    Account save(Account account) throws NoSuchAlgorithmException;
}
