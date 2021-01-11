package nitpicksy.paymentgateway.mapper;

import nitpicksy.paymentgateway.dto.request.DataForPaymentRequestDTO;
import nitpicksy.paymentgateway.dto.request.PaymentDataRequestDTO;
import nitpicksy.paymentgateway.model.Data;
import nitpicksy.paymentgateway.model.DataForPayment;
import nitpicksy.paymentgateway.model.Merchant;
import nitpicksy.paymentgateway.model.PaymentMethod;
import nitpicksy.paymentgateway.service.DataService;
import nitpicksy.paymentgateway.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentDataRequestMapper {

    private DataService dataService;

    private PaymentMethodService paymentMethodService;

    public List<DataForPayment> convert(List<PaymentDataRequestDTO> listPaymentDataRequest, Merchant merchant) throws NoSuchAlgorithmException {
        List<DataForPayment> dataForPayments = new ArrayList<>();
        for (PaymentDataRequestDTO paymentDataRequestDTO : listPaymentDataRequest) {
            PaymentMethod paymentMethod = paymentMethodService.findById(paymentDataRequestDTO.getPaymentMethod().getId());
            dataForPayments.addAll(convertPaymentData(paymentMethod, paymentDataRequestDTO.getPaymentData(), merchant));
        }
        return dataForPayments;
    }

    public List<DataForPayment> convertPaymentData(PaymentMethod paymentMethod, List<DataForPaymentRequestDTO> listDataForPayment, Merchant merchant) throws NoSuchAlgorithmException {
        List<DataForPayment> dataForPayments = new ArrayList<>();
        for (DataForPaymentRequestDTO dataForPaymentRequestDTO : listDataForPayment) {
            DataForPayment dataForPayment = new DataForPayment();
            Data data = dataService.findById(dataForPaymentRequestDTO.getPaymentDataId());
            dataForPayment.setAttributeName(data.getAttributeJSONName());
            dataForPayment.setAttributeValue(dataForPaymentRequestDTO.getAttributeValue());
            dataForPayment.setPaymentMethod(paymentMethod);
            dataForPayment.setMerchant(merchant);
            dataForPayment.setData(data);
            dataForPayments.add(dataForPayment);
        }
        return dataForPayments;
    }

    @Autowired
    public PaymentDataRequestMapper(DataService dataService, PaymentMethodService paymentMethodService) {
        this.dataService = dataService;
        this.paymentMethodService = paymentMethodService;
    }
}
