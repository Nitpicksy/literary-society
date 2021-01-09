package nitpicksy.paymentgateway.service;

import nitpicksy.paymentgateway.model.Transaction;

import java.util.List;

public interface TransactionService {

    List<Transaction> findAll(Long companyId);
}
