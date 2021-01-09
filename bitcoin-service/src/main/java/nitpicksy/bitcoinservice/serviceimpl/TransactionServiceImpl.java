package nitpicksy.bitcoinservice.serviceimpl;


import nitpicksy.bitcoinservice.enumeration.TransactionStatus;
import nitpicksy.bitcoinservice.model.Log;
import nitpicksy.bitcoinservice.model.Payment;
import nitpicksy.bitcoinservice.model.Transaction;
import nitpicksy.bitcoinservice.repository.TransactionRepository;
import nitpicksy.bitcoinservice.service.LogService;
import nitpicksy.bitcoinservice.service.TransactionService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private TransactionRepository transactionRepository;

    private LogService logService;

    @Override
    public Transaction findByPaymentId(Long id) {
        return transactionRepository.findByPaymentId(id);
    }

    @Override
    public Transaction createErrorTransaction(Payment paymentRequest) {
        Transaction transaction = new Transaction(paymentRequest.getAmount(), paymentRequest.getMerchantOrderId(),
                paymentRequest.getMerchantTimestamp(),paymentRequest.getId());

        transaction.setStatus(TransactionStatus.ERROR);
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public void create(Payment paymentRequest, TransactionStatus status) {
        Transaction transaction = new Transaction(paymentRequest.getAmount(), paymentRequest.getMerchantOrderId(),
                paymentRequest.getMerchantTimestamp(),paymentRequest.getId(),status);
        Transaction savedTransaction = transactionRepository.save(transaction);
        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CONFPAY", String.format("Transaction %s is created.Status of this transaction is %s", savedTransaction.getId(), savedTransaction.getStatus())));
    }

    public TransactionServiceImpl(TransactionRepository transactionRepository,LogService logService) {
        this.transactionRepository = transactionRepository;
        this.logService = logService;
    }
}
