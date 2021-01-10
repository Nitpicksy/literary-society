package nitpicksy.paymentgateway.service;

import nitpicksy.paymentgateway.dto.request.ConfirmPaymentRequestDTO;
import nitpicksy.paymentgateway.dto.request.OrderRequestDTO;
import nitpicksy.paymentgateway.dto.request.PaymentRequestDTO;
import nitpicksy.paymentgateway.enumeration.TransactionStatus;
import nitpicksy.paymentgateway.model.Transaction;

public interface OrderService {

    String createOrder(OrderRequestDTO orderDTO);

    Transaction findOrder(Long orderId);

    String forwardPaymentRequest(PaymentRequestDTO paymentRequestDTO);

    void cancelOrder(Long id);

    void setPayment(Long orderId, Long paymentId);

    void handleConfirmPayment(String merchantOrderId, ConfirmPaymentRequestDTO dto);

    void notifyCompany(Transaction order, String status);

    Transaction setTransactionStatus(Transaction transaction, TransactionStatus status);

    void synchronizeTransactions();
}
