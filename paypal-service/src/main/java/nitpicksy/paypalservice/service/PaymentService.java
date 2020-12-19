package nitpicksy.paypalservice.service;

import nitpicksy.paypalservice.dto.response.PaymentResponseDTO;
import nitpicksy.paypalservice.model.PaymentRequest;

public interface PaymentService {

    PaymentResponseDTO createPayment(PaymentRequest paymentRequest);

    String executePayment(String paymentId, String payerId);

}
