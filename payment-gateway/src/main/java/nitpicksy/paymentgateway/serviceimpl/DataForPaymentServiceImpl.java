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
    public List<DataForPayment> save(List<DataForPayment> dataForPayments) {
        return dataForPaymentRepository.saveAll(dataForPayments);
    }

    @Autowired
    public DataForPaymentServiceImpl(DataForPaymentRepository dataForPaymentRepository) {
        this.dataForPaymentRepository = dataForPaymentRepository;
    }
}
