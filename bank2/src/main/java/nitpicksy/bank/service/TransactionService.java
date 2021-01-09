package nitpicksy.bank.service;

import nitpicksy.bank.dto.request.ConfirmPaymentDTO;
import nitpicksy.bank.dto.response.ConfirmPaymentResponseDTO;
import nitpicksy.bank.enumeration.TransactionStatus;
import nitpicksy.bank.model.CreditCard;
import nitpicksy.bank.model.PaymentRequest;
import nitpicksy.bank.model.Transaction;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.List;

public interface TransactionService {

    Transaction transferInsideBank(PaymentRequest paymentRequest, CreditCard creditCard) throws NoSuchAlgorithmException;

    Transaction findByMerchantOrderId(String id);

    ConfirmPaymentResponseDTO transferBetweenBanks(PaymentRequest paymentRequest, ConfirmPaymentDTO confirmPaymentDTO) throws NoSuchAlgorithmException;

    Transaction create(Double amount, String merchantId, String merchantOrderId, Timestamp merchantTimestamp, Long paymentId, String pan, TransactionStatus status);

    Transaction findByPaymentId(Long id);

    Transaction createErrorTransaction(PaymentRequest paymentRequest);

    List<Transaction> findAll();
}
