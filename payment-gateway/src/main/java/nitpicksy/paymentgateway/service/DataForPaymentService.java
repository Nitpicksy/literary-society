package nitpicksy.paymentgateway.service;

import nitpicksy.paymentgateway.model.DataForPayment;

import java.util.List;

public interface DataForPaymentService {

    List<DataForPayment> findDataForPaymentByMerchant(Long merchantId);

    List<DataForPayment> save(List<DataForPayment> dataForPayments);
}
