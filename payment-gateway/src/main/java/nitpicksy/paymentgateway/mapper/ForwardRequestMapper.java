package nitpicksy.paymentgateway.mapper;

import nitpicksy.paymentgateway.dto.request.DynamicPaymentDetailsDTO;
import nitpicksy.paymentgateway.model.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ForwardRequestMapper implements MapperInterface<Transaction, DynamicPaymentDetailsDTO> {

    private final ModelMapper modelMapper;

    @Override
    public Transaction toEntity(DynamicPaymentDetailsDTO dto) {
        return null;
    }

    @Override
    public DynamicPaymentDetailsDTO toDto(Transaction entity) {
        DynamicPaymentDetailsDTO dto = modelMapper.map(entity, DynamicPaymentDetailsDTO.class);
        dto.setAmount(entity.getAmount());
        Long merchantOrderId = Long.valueOf( entity.getMerchantOrderId().split("::")[1]);

        dto.setSuccessURL(entity.getCompany().getSuccessURL() + '/' + merchantOrderId);
        dto.setFailedURL(entity.getCompany().getFailedURL());
        dto.setErrorURL(entity.getCompany().getErrorURL());
        dto.setMerchantTimestamp(entity.getMerchantTimestamp().toString());

        dto.setMerchantOrderId(entity.getMerchantOrderId());
        return dto;
    }

    @Autowired
    public ForwardRequestMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

}
