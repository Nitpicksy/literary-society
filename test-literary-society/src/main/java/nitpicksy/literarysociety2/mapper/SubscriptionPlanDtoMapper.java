package nitpicksy.literarysociety2.mapper;

import nitpicksy.literarysociety2.dto.request.SubscriptionPlanDTO;
import nitpicksy.literarysociety2.model.SubscriptionPlan;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionPlanDtoMapper implements MapperInterface<SubscriptionPlan, SubscriptionPlanDTO> {

    private final ModelMapper modelMapper;

    @Override
    public SubscriptionPlan toEntity(SubscriptionPlanDTO dto) {
        return modelMapper.map(dto, SubscriptionPlan.class);
    }

    @Override
    public SubscriptionPlanDTO toDto(SubscriptionPlan entity) {
        return modelMapper.map(entity, SubscriptionPlanDTO.class);
    }

    @Autowired
    public SubscriptionPlanDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
