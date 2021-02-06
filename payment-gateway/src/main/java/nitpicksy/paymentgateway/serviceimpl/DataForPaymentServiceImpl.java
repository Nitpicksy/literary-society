package nitpicksy.paymentgateway.serviceimpl;

import nitpicksy.paymentgateway.model.DataForPayment;
import nitpicksy.paymentgateway.repository.DataForPaymentRepository;
import nitpicksy.paymentgateway.service.DataForPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataForPaymentServiceImpl implements DataForPaymentService {

    private DataForPaymentRepository dataForPaymentRepository;


    @Override
    public List<DataForPayment> findDataForPaymentByMerchant(Long merchantId) {
        return dataForPaymentRepository.findByMerchantId(merchantId);
    }

    @Override
    public DataForPayment findByAttribute(Long merchantId, String paymentMethodCommonName, String attributeName) {
        return dataForPaymentRepository.findByMerchantIdAndPaymentMethodCommonNameAndAttributeName(
                merchantId, paymentMethodCommonName, attributeName);
    }

    @Override
    public List<DataForPayment> save(List<DataForPayment> dataForPayments) {
        return dataForPaymentRepository.saveAll(dataForPayments);
    }

    @Override
    public void deleteCompanyDataForPayment(Long paymentMethodId, Long companyId) {
        List<DataForPayment> dataForPayments = dataForPaymentRepository.findByPaymentMethodIdAndMerchantCompanyId(paymentMethodId,companyId);
        dataForPaymentRepository.deleteAll(dataForPayments);
    }

    @Autowired
    public DataForPaymentServiceImpl(DataForPaymentRepository dataForPaymentRepository) {
        this.dataForPaymentRepository = dataForPaymentRepository;
    }
}
