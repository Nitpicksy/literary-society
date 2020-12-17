package nitpicksy.paymentgateway.service;

import nitpicksy.paymentgateway.dto.request.ConfirmPaymentRequestDTO;
import nitpicksy.paymentgateway.dto.request.DynamicPaymentDetailsDTO;
import nitpicksy.paymentgateway.dto.request.OrderRequestDTO;
import nitpicksy.paymentgateway.model.Transaction;

public interface OrderService {

    Transaction createOrder(OrderRequestDTO orderDTO);

    Transaction findOrder(Long orderId);

    DynamicPaymentDetailsDTO forwardPaymentRequest(Long orderId, String paymentMethodCommonName);

    void cancelOrder(Long id);

    void setPayment(Long orderId, Long paymentId);

    void handleConfirmPayment(Long merchantOrderId, ConfirmPaymentRequestDTO dto);
}
