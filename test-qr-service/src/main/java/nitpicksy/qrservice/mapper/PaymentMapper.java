package nitpicksy.qrservice.mapper;

import nitpicksy.qrservice.dto.request.PaymentDetailsDTO;
import nitpicksy.qrservice.dto.request.PaymentRequestDTO;
import nitpicksy.qrservice.model.Payment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class PaymentMapper implements MapperInterface<Payment, PaymentRequestDTO> {

    private ModelMapper modelMapper;

    @Override
    public Payment toEntity(PaymentRequestDTO dto) {
        Payment request = new Payment();

        request.setAmount(dto.getAmount());
        request.setErrorURL(dto.getErrorURL());
        request.setFailedURL(dto.getFailedURL());
        request.setSuccessURL(dto.getSuccessURL());
        request.setMerchantOrderId(dto.getMerchantOrderId());
        request.setMerchantTimestamp(Timestamp.valueOf(dto.getMerchantTimestamp()));
        request.setMerchantToken(dto.getPaymentDetails().getMerchantToken());
        return request;
    }

    @Override
    public PaymentRequestDTO toDto(Payment entity) {
        PaymentRequestDTO dto = modelMapper.map(entity, PaymentRequestDTO.class);
        dto.setMerchantTimestamp(entity.getMerchantTimestamp().toString());
        dto.setPaymentDetails(new PaymentDetailsDTO(entity.getMerchantToken()));
        return dto;
    }

    @Autowired
    public PaymentMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}