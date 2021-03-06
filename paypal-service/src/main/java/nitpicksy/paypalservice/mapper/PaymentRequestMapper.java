package nitpicksy.paypalservice.mapper;

import nitpicksy.paypalservice.dto.request.PaymentDetailsDTO;
import nitpicksy.paypalservice.dto.request.PaymentRequestDTO;
import nitpicksy.paypalservice.model.PaymentRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class PaymentRequestMapper implements MapperInterface<PaymentRequest, PaymentRequestDTO> {

    private ModelMapper modelMapper;

    @Override
    public PaymentRequest toEntity(PaymentRequestDTO dto) {
        PaymentRequest request = new PaymentRequest();
        request.setMerchantClientId(dto.getPaymentDetails().getMerchantClientId());
        request.setMerchantClientSecret(dto.getPaymentDetails().getMerchantClientSecret());
        request.setMerchantOrderId(dto.getMerchantOrderId());
        request.setMerchantTimestamp(Timestamp.valueOf(dto.getMerchantTimestamp()));
        request.setAmount(dto.getAmount());

        request.setSuccessURL(dto.getSuccessURL());
        request.setErrorURL(dto.getErrorURL());
        request.setFailedURL(dto.getFailedURL());

        request.setMerchantTimestamp(Timestamp.valueOf(dto.getMerchantTimestamp()));

        return request;
    }

    @Override
    public PaymentRequestDTO toDto(PaymentRequest entity) {
        PaymentRequestDTO dto = modelMapper.map(entity, PaymentRequestDTO.class);
        dto.setMerchantTimestamp(entity.getMerchantTimestamp().toString());
        dto.setPaymentDetails(new PaymentDetailsDTO(entity.getMerchantClientId(), entity.getMerchantClientSecret()));
        return dto;
    }

    @Autowired
    public PaymentRequestMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
