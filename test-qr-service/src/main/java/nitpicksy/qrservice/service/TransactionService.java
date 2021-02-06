package nitpicksy.qrservice.service;

import nitpicksy.qrservice.enumeration.TransactionStatus;
import nitpicksy.qrservice.model.Payment;
import nitpicksy.qrservice.model.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction findByPaymentId(Long id);

    Transaction createErrorTransaction(Payment paymentRequest);

    List<Transaction> findAll();

    void create(Payment paymentRequest, TransactionStatus status);
}
