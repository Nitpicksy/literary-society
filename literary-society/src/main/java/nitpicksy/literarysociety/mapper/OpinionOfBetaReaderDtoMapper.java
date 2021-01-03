package nitpicksy.literarysociety.mapper;

import nitpicksy.literarysociety.dto.response.OpinionOfBetaReaderDTO;
import nitpicksy.literarysociety.model.OpinionOfBetaReader;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OpinionOfBetaReaderDtoMapper implements MapperInterface<OpinionOfBetaReader, OpinionOfBetaReaderDTO> {

    private final ModelMapper modelMapper;

    @Override
    public OpinionOfBetaReader toEntity(OpinionOfBetaReaderDTO dto) {
        return modelMapper.map(dto, OpinionOfBetaReader.class);
    }

    @Override
    public OpinionOfBetaReaderDTO toDto(OpinionOfBetaReader entity) {
        OpinionOfBetaReaderDTO dto = modelMapper.map(entity, OpinionOfBetaReaderDTO.class);
        dto.setBetaReadersName(entity.getBetaReader().getFirstName() + " " + entity.getBetaReader().getLastName());
        return dto;
    }

    @Autowired
    public OpinionOfBetaReaderDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
