package nitpicksy.bank.service;

import nitpicksy.bank.model.CreditCard;
import nitpicksy.bank.model.PaymentRequest;
import nitpicksy.bank.model.Transaction;

import java.security.NoSuchAlgorithmException;

public interface TransactionService {

    Transaction transferInsideBank(PaymentRequest paymentRequest, CreditCard creditCard) throws NoSuchAlgorithmException;

    Transaction findByMerchantOrderId(Long id);
}
