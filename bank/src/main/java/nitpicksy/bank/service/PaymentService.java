package nitpicksy.bank.service;

import nitpicksy.bank.dto.request.ConfirmPaymentDTO;
import nitpicksy.bank.dto.response.PaymentResponseDTO;
import nitpicksy.bank.model.PaymentRequest;

import java.security.NoSuchAlgorithmException;

public interface PaymentService {

    PaymentResponseDTO pay(PaymentRequest paymentRequest);

    String confirmPayment(ConfirmPaymentDTO confirmPaymentDTO) throws NoSuchAlgorithmException;
}
