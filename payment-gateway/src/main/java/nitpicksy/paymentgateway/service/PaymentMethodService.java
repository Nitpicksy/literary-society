package nitpicksy.paymentgateway.service;

import nitpicksy.paymentgateway.model.PaymentMethod;

import java.util.List;

public interface PaymentMethodService {

    List<PaymentMethod> findMerchantPaymentMethods(Long orderId);

}
