package nitpicksy.bank.serviceImpl;
import nitpicksy.bank.exceptionHandler.InvalidDataException;
import nitpicksy.bank.model.Account;
import nitpicksy.bank.model.CreditCard;
import nitpicksy.bank.model.Log;
import nitpicksy.bank.repository.AccountRepository;
import nitpicksy.bank.service.AccountService;
import nitpicksy.bank.service.CreditCardService;
import nitpicksy.bank.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    private CreditCardService creditCardService;

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private LogService logService;

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

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account save(Account account) throws NoSuchAlgorithmException {
        if(accountRepository.findByEmail(account.getEmail()) != null){
            throw new InvalidDataException("Client with same email address already exist.", HttpStatus.BAD_REQUEST);
        }

        CreditCard creditCard = creditCardService.create(account);
        account.getCreditCards().add(creditCard);
        Account savedAccount = accountRepository.save(account);

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CLIENTA", String.format("Client account %s is created.", savedAccount.getId())));
        return savedAccount;
    }

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, CreditCardService creditCardService,LogService logService) {
        this.accountRepository = accountRepository;
        this.creditCardService = creditCardService;
        this.logService = logService;
    }
}
