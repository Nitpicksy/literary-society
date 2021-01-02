package nitpicksy.paymentgateway.service;

import nitpicksy.paymentgateway.model.Data;
import nitpicksy.paymentgateway.model.PaymentMethod;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Set;

public interface PaymentMethodService {

    List<PaymentMethod> findMerchantPaymentMethods(Long orderId);

    PaymentMethod findPaymentMethod(String commonName);

    PaymentMethod registerPaymentMethod(PaymentMethod paymentMethod, Set<Data> listData);

    List<PaymentMethod> findAll();

    List<PaymentMethod> findAllApproved();

    List<PaymentMethod> findByIds(List<Long> ids);

    PaymentMethod changePaymentMethodStatus(Long id, String status);
}
