package nitpicksy.literarysociety.mapper;

import nitpicksy.literarysociety.dto.response.OpinionDTO;
import nitpicksy.literarysociety.model.OpinionOfBetaReader;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OpinionOfBetaReaderDtoMapper implements MapperInterface<OpinionOfBetaReader, OpinionDTO> {

    private final ModelMapper modelMapper;

    @Override
    public OpinionOfBetaReader toEntity(OpinionDTO dto) {
        return modelMapper.map(dto, OpinionOfBetaReader.class);
    }

    @Override
    public OpinionDTO toDto(OpinionOfBetaReader entity) {
        OpinionDTO dto = modelMapper.map(entity, OpinionDTO.class);
        dto.setCommenterName(entity.getBetaReader().getFirstName() + " " + entity.getBetaReader().getLastName());
        return dto;
    }

    @Autowired
    public OpinionOfBetaReaderDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
