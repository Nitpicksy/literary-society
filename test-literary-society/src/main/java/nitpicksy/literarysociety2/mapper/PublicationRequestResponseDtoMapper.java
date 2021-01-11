package nitpicksy.literarysociety2.mapper;

import nitpicksy.literarysociety2.dto.response.PublicationRequestResponseDTO;
import nitpicksy.literarysociety2.model.Book;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PublicationRequestResponseDtoMapper implements MapperInterface<Book, PublicationRequestResponseDTO> {

    private ModelMapper modelMapper;

    /**
     * Not properly mapped direction
     */
    @Override
    public Book toEntity(PublicationRequestResponseDTO dto) {
        return modelMapper.map(dto, Book.class);
    }

    @Override
    public PublicationRequestResponseDTO toDto(Book entity) {
        PublicationRequestResponseDTO dto = new PublicationRequestResponseDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setGenre(entity.getGenre().getName());
        dto.setSynopsis(entity.getSynopsis());
        dto.setEditorName(entity.getEditor().getFirstName() + " " + entity.getEditor().getLastName());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    @Autowired
    public PublicationRequestResponseDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

}
