package nitpicksy.paymentgateway.serviceimpl;

import nitpicksy.paymentgateway.model.PaymentMethod;
import nitpicksy.paymentgateway.service.PaymentMethodService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    public List<PaymentMethod> findMerchantPaymentMethods(String orderId) {
        return null;
    }

}
