package nitpicksy.paypalservice.service;

import nitpicksy.paypalservice.enumeration.TransactionStatus;
import nitpicksy.paypalservice.model.PaymentRequest;
import nitpicksy.paypalservice.model.Transaction;

import java.sql.Timestamp;
import java.util.List;

public interface TransactionService {

    Transaction create(Double amount, String merchantOrderId, Timestamp merchantTimestamp, Long paymentId,TransactionStatus status);

    Transaction findByPaymentId(Long id);

    Transaction createErrorTransaction(PaymentRequest paymentRequest);

    List<Transaction> findAll();

    void create(PaymentRequest paymentRequest, TransactionStatus status);
}
