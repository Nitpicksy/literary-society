package nitpicksy.bitcoinservice.service;

import nitpicksy.bitcoinservice.enumeration.TransactionStatus;
import nitpicksy.bitcoinservice.model.Payment;
import nitpicksy.bitcoinservice.model.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction findByPaymentId(Long id);

    Transaction createErrorTransaction(Payment paymentRequest);

    List<Transaction> findAll();

    void create(Payment paymentRequest, TransactionStatus status);
}
