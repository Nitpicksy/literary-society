package nitpicksy.paypalservice.mapper;

import nitpicksy.paypalservice.dto.request.PaymentDetailsDTO;
import nitpicksy.paypalservice.dto.request.PaymentRequestDTO;
import nitpicksy.paypalservice.dto.request.SubscriptionPlanDTO;
import nitpicksy.paypalservice.model.PaymentRequest;
import nitpicksy.paypalservice.model.SubscriptionPlan;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class SubscriptionPlanMapper implements MapperInterface<SubscriptionPlan, SubscriptionPlanDTO> {

    private ModelMapper modelMapper;

    @Override
    public SubscriptionPlan toEntity(SubscriptionPlanDTO dto) {
        SubscriptionPlan entity = new SubscriptionPlan(null, null, dto.getMerchantClientId(), dto.getMerchantClientSecret(),
                dto.getProductName(), dto.getProductType(), dto.getProductCategory(), dto.getPlanName(), dto.getPlanDescription(),
                dto.getPrice(), dto.getFrequencyUnit(), dto.getFrequencyCount(), dto.getSuccessURL(), dto.getCancelURL());
        return entity;
    }

    @Override
    public SubscriptionPlanDTO toDto(SubscriptionPlan entity) {
        return modelMapper.map(entity, SubscriptionPlanDTO.class);
    }

    @Autowired
    public SubscriptionPlanMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
