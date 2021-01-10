package nitpicksy.literarysociety.mapper;

import nitpicksy.literarysociety.dto.request.SubscriptionPlanDTO;
import nitpicksy.literarysociety.dto.response.SubscriptionPlanResponseDTO;
import nitpicksy.literarysociety.model.SubscriptionPlan;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionPlanResponseDtoMapper implements MapperInterface<SubscriptionPlan, SubscriptionPlanResponseDTO> {

    private final ModelMapper modelMapper;

    @Override
    public SubscriptionPlan toEntity(SubscriptionPlanResponseDTO dto) {
        return modelMapper.map(dto, SubscriptionPlan.class);
    }

    @Override
    public SubscriptionPlanResponseDTO toDto(SubscriptionPlan entity) {
        return modelMapper.map(entity, SubscriptionPlanResponseDTO.class);
    }

    @Autowired
    public SubscriptionPlanResponseDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
