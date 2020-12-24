package nitpicksy.paymentgateway.service;

import nitpicksy.paymentgateway.model.Data;
import nitpicksy.paymentgateway.model.PaymentMethod;

import java.util.List;
import java.util.Set;

public interface PaymentMethodService {

    List<PaymentMethod> findMerchantPaymentMethods(Long orderId);
    
    PaymentMethod findPaymentMethod(String commonName);

    PaymentMethod registerPaymentMethod(PaymentMethod paymentMethod, Set<Data> listData);
}
