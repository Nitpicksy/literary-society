package nitpicksy.bank.serviceImpl;

import nitpicksy.bank.enumeration.TransactionStatus;
import nitpicksy.bank.exceptionHandler.InvalidDataException;
import nitpicksy.bank.model.Account;
import nitpicksy.bank.model.CreditCard;
import nitpicksy.bank.model.Merchant;
import nitpicksy.bank.model.PaymentRequest;
import nitpicksy.bank.model.Transaction;
import nitpicksy.bank.repository.TransactionRepository;
import nitpicksy.bank.service.AccountService;
import nitpicksy.bank.service.MerchantService;
import nitpicksy.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;

@Service
public class TransactionServiceImpl implements TransactionService {

    private HashValueServiceImpl hashValueServiceImpl;

    private TransactionRepository transactionRepository;

    private AccountService accountService;

    private MerchantService merchantService;

    @Override
    public Transaction transferInsideBank(PaymentRequest paymentRequest, CreditCard creditCard)  {
        Transaction transaction;
            transaction = create(paymentRequest,creditCard.getPan());
        try {
            transfer(creditCard,paymentRequest.getMerchantId(),paymentRequest.getAmount());
        } catch (NoSuchAlgorithmException e) {
            return setTransactionStatus(transaction,TransactionStatus.ERROR);
        }catch (InvalidDataException e){
            return setTransactionStatus(transaction,TransactionStatus.FAILED);
        }

        return setTransactionStatus(transaction,TransactionStatus.SUCCESS);
    }

    @Override
    public Transaction findByMerchantOrderId(Long id) {
        return transactionRepository.findByMerchantOrderId(id);
    }

    private Transaction create(PaymentRequest paymentRequest, String pan)  {
        return new Transaction(paymentRequest.getAmount(), paymentRequest.getMerchantId(), paymentRequest.getMerchantOrderId(),
                paymentRequest.getMerchantTimestamp(),pan,paymentRequest.getId());
    }

    @Transactional
    public void transfer(CreditCard buyer, String merchantId, Double amount) throws NoSuchAlgorithmException {
        if(!accountService.hasEnoughMoney(amount,buyer)){
            throw new InvalidDataException("You don't have enough money.", HttpStatus.BAD_REQUEST);
        }
        merchantService.transferMoneyToMerchant(merchantId,amount);
    }

    private Transaction setTransactionStatus(Transaction transaction, TransactionStatus status){
        transaction.setStatus(status);
        transactionRepository.save(transaction);
        return transaction;
    }

    @Autowired
    public TransactionServiceImpl(HashValueServiceImpl hashValueServiceImpl, TransactionRepository transactionRepository,AccountService accountService,
                                  MerchantService merchantService) {
        this.hashValueServiceImpl = hashValueServiceImpl;
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.merchantService = merchantService;
    }
}
