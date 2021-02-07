package nitpicksy.qrservice.serviceimpl;


import nitpicksy.qrservice.enumeration.TransactionStatus;
import nitpicksy.qrservice.model.Log;
import nitpicksy.qrservice.model.Payment;
import nitpicksy.qrservice.model.Transaction;
import nitpicksy.qrservice.repository.TransactionRepository;
import nitpicksy.qrservice.service.LogService;
import nitpicksy.qrservice.service.TransactionService;
import org.springframework.stereotype.Service;

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
                paymentRequest.getMerchantTimestamp(), paymentRequest.getId());

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
                paymentRequest.getMerchantTimestamp(), paymentRequest.getId(), status);
        Transaction savedTransaction = transactionRepository.save(transaction);
        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CONFPAY", String.format("Transaction %s is created.Status of this transaction is %s", savedTransaction.getId(), savedTransaction.getStatus())));
    }

    public TransactionServiceImpl(TransactionRepository transactionRepository, LogService logService) {
        this.transactionRepository = transactionRepository;
        this.logService = logService;
    }
}

