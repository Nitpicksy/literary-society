package nitpicksy.bank.service;

import nitpicksy.bank.dto.request.PayRequestDTO;
import nitpicksy.bank.model.CreditCard;
import nitpicksy.bank.model.Transaction;

public interface AccountService {

    boolean hasEnoughMoney(Double amount, CreditCard creditCard);

}
