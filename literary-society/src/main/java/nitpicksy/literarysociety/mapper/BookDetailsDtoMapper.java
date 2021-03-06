package nitpicksy.literarysociety.mapper;

import nitpicksy.literarysociety.dto.response.BookDetailsDTO;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.PublishingInfo;
import nitpicksy.literarysociety.service.ImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookDetailsDtoMapper implements MapperInterface<Book, BookDetailsDTO> {

    private BookDtoMapper bookDtoMapper;

    /**
     * Not properly mapped direction
     *
     * @param dto
     * @return
     */
    @Override
    public Book toEntity(BookDetailsDTO dto) {
        Book entity = new Book();
        return entity;
    }

    @Override
    public BookDetailsDTO toDto(Book entity) {
        BookDetailsDTO dto = new BookDetailsDTO();
        dto.setBookDTO(bookDtoMapper.toDto(entity));
        dto.setSynopsis(entity.getSynopsis());
        PublishingInfo publishingInfo = entity.getPublishingInfo();
        dto.setPublicationDate(publishingInfo.getPublicationDate().toString());
        dto.setISBN(publishingInfo.getISBN());
        dto.setPublisherCity(publishingInfo.getPublisherCity());
        dto.setPublisher(publishingInfo.getPublisher());
        if(entity.getGenre() != null){
            dto.setGenreName(entity.getGenre().getName());
        }
        return dto;
    }

    @Autowired
    public BookDetailsDtoMapper(BookDtoMapper bookDtoMapper) {
        this.bookDtoMapper = bookDtoMapper;
    }
}

