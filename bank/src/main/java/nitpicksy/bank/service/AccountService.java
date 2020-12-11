package nitpicksy.bank.service;

import nitpicksy.bank.model.CreditCard;

public interface AccountService {

    boolean hasEnoughMoney(Double amount, CreditCard creditCard);
}
