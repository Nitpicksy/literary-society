package nitpicksy.literarysociety2.mapper;

import nitpicksy.literarysociety2.dto.response.OpinionDTO;
import nitpicksy.literarysociety2.model.OpinionOfEditor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OpinionOfEditorDtoMapper implements MapperInterface<OpinionOfEditor, OpinionDTO> {

    private final ModelMapper modelMapper;

    @Override
    public OpinionOfEditor toEntity(OpinionDTO dto) {
        return modelMapper.map(dto, OpinionOfEditor.class);
    }

    @Override
    public OpinionDTO toDto(OpinionOfEditor entity) {
        OpinionDTO dto = modelMapper.map(entity, OpinionDTO.class);
        dto.setCommenterName(entity.getEditor().getFirstName() + " " + entity.getEditor().getLastName());
        return dto;
    }

    @Autowired
    public OpinionOfEditorDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
