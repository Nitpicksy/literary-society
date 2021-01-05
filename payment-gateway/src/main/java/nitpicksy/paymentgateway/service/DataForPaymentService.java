package nitpicksy.paymentgateway.service;

import nitpicksy.paymentgateway.model.DataForPayment;

import java.util.List;

public interface DataForPaymentService {

    List<DataForPayment> findDataForPaymentByMerchant(Long merchantId);

    DataForPayment findByAttribute(Long merchantId, String paymentMethodCommonName, String attributeName);

    List<DataForPayment> save(List<DataForPayment> dataForPayments);
}
