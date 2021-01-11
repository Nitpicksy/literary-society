package nitpicksy.bitcoinservice.service;

import nitpicksy.bitcoinservice.dto.request.PaymentCallbackDTO;
import nitpicksy.bitcoinservice.dto.response.PaymentResponseDTO;
import nitpicksy.bitcoinservice.model.Payment;

public interface PaymentService {

    PaymentResponseDTO createPayment(Payment paymentRequest);

    void executePayment(PaymentCallbackDTO callbackDTO);

}
