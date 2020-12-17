package nitpicksy.paymentgateway.service;

import nitpicksy.paymentgateway.dto.request.ConfirmPaymentRequestDTO;
import nitpicksy.paymentgateway.dto.request.OrderRequestDTO;
import nitpicksy.paymentgateway.dto.request.PaymentRequestDTO;
import nitpicksy.paymentgateway.model.Transaction;

public interface OrderService {

    String createOrder(OrderRequestDTO orderDTO);

    Transaction findOrder(Long orderId);

    String forwardPaymentRequest(PaymentRequestDTO paymentRequestDTO);

    void cancelOrder(Long id);

    void setPayment(Long orderId, Long paymentId);

    void handleConfirmPayment(Long merchantOrderId, ConfirmPaymentRequestDTO dto);
}
