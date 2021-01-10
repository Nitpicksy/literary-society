package nitpicksy.literarysociety.mapper;

import nitpicksy.literarysociety.dto.request.SubscriptionPlanDTO;
import nitpicksy.literarysociety.dto.response.OpinionDTO;
import nitpicksy.literarysociety.model.OpinionOfBetaReader;
import nitpicksy.literarysociety.model.SubscriptionPlan;
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
