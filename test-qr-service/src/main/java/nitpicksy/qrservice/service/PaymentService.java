package nitpicksy.qrservice.service;

import nitpicksy.qrservice.dto.request.PaymentCallbackDTO;
import nitpicksy.qrservice.dto.response.PaymentResponseDTO;
import nitpicksy.qrservice.model.Payment;

public interface PaymentService {

    PaymentResponseDTO createPayment(Payment paymentRequest);

    void executePayment(PaymentCallbackDTO callbackDTO);

}
