package nitpicksy.paymentgateway.serviceimpl;

import nitpicksy.paymentgateway.dto.response.PaymentMethodResponseDTO;
import nitpicksy.paymentgateway.exceptionHandler.InvalidDataException;
import nitpicksy.paymentgateway.model.PaymentMethod;
import nitpicksy.paymentgateway.model.Transaction;
import nitpicksy.paymentgateway.repository.PaymentMethodRepository;
import nitpicksy.paymentgateway.service.OrderService;
import nitpicksy.paymentgateway.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private OrderService orderService;

    private PaymentMethodRepository paymentMethodRepository;

    public List<PaymentMethod> findMerchantPaymentMethods(Long orderId) {

        Transaction order = orderService.findOrder(orderId);
        if (order == null) {
            throw new InvalidDataException("Order is invalid or doesn't exist.", HttpStatus.BAD_REQUEST);
        }

        List<PaymentMethod> paymentMethods = new ArrayList<>(order.getCompany().getPaymentMethods());
        if (paymentMethods.isEmpty()) {
            throw new InvalidDataException("No payment methods registered to order.", HttpStatus.BAD_REQUEST);
        }

        return paymentMethods;
    }

    @Override
    public PaymentMethod findPaymentMethod(String commonName) {
        return paymentMethodRepository.findByCommonName(commonName);
    }

    @Autowired
    public PaymentMethodServiceImpl(OrderService orderService, PaymentMethodRepository paymentMethodRepository) {
        this.orderService = orderService;
        this.paymentMethodRepository = paymentMethodRepository;
    }
}
