package nitpicksy.paypalservice.service;

import com.paypal.base.rest.PayPalRESTException;
import nitpicksy.paypalservice.dto.request.ConfirmPaymentDTO;
import nitpicksy.paypalservice.dto.request.PaymentRequestDTO;
import nitpicksy.paypalservice.dto.response.PaymentResponseDTO;

public interface PaymentService {

    PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO) throws PayPalRESTException;

    String completePayment(String paymentId, ConfirmPaymentDTO confirmPaymentDTO) throws PayPalRESTException;

}
