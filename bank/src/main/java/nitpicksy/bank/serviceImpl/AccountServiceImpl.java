package nitpicksy.bank.serviceImpl;

import nitpicksy.bank.model.Account;
import nitpicksy.bank.model.CreditCard;
import nitpicksy.bank.repository.AccountRepository;
import nitpicksy.bank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    @Override
    public boolean hasEnoughMoney(Double amount, CreditCard creditCard) {
        Account account = accountRepository.findByCreditCardsId(creditCard.getId());
        if(account != null && account.getBalance() >= amount){
            account.setBalance(account.getBalance() - amount);
            accountRepository.save(account);
            return true;
        }
        return false;
    }

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}
