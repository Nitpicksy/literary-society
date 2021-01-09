package nitpicksy.paypalservice.serviceimpl;

import nitpicksy.paypalservice.enumeration.TransactionStatus;
import nitpicksy.paypalservice.model.Log;
import nitpicksy.paypalservice.model.PaymentRequest;
import nitpicksy.paypalservice.model.Transaction;
import nitpicksy.paypalservice.repository.TransactionRepository;
import nitpicksy.paypalservice.service.LogService;
import nitpicksy.paypalservice.service.TransactionService;
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
    public Transaction create(Double amount, String merchantOrderId, Timestamp merchantTimestamp, Long paymentId, TransactionStatus status) {
        Transaction transaction =  new Transaction(amount, merchantOrderId, merchantTimestamp,paymentId,status);
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction findByPaymentId(Long id) {
        return transactionRepository.findByPaymentId(id);
    }

    @Override
    public Transaction createErrorTransaction(PaymentRequest paymentRequest) {
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
    public void create(PaymentRequest paymentRequest, TransactionStatus status) {
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
