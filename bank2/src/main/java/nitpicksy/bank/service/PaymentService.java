package nitpicksy.bank.service;

import nitpicksy.bank.dto.request.ConfirmPaymentDTO;
import nitpicksy.bank.dto.request.PayRequestDTO;
import nitpicksy.bank.dto.response.PaymentResponseDTO;
import nitpicksy.bank.model.PaymentRequest;
import nitpicksy.bank.model.Transaction;

import java.security.NoSuchAlgorithmException;

public interface PaymentService {

    PaymentResponseDTO pay(PaymentRequest paymentRequest);

    String confirmPayment(ConfirmPaymentDTO confirmPaymentDTO,Long paymentId) throws NoSuchAlgorithmException;

    Transaction payClientBank(PayRequestDTO payRequestDTO);
}
