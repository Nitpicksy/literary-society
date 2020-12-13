package nitpicksy.paymentgateway.service;

import nitpicksy.paymentgateway.dto.request.OrderRequestDTO;
import nitpicksy.paymentgateway.model.Transaction;

public interface OrderService {

    Transaction createOrder(OrderRequestDTO orderDTO);
}
