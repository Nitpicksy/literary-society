package nitpicksy.literarysociety2.mapper;

import nitpicksy.literarysociety2.dto.response.BookDTO;
import nitpicksy.literarysociety2.model.Book;
import nitpicksy.literarysociety2.model.PublishingInfo;
import nitpicksy.literarysociety2.service.ImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookDtoMapper implements MapperInterface<Book, BookDTO> {

    private ModelMapper modelMapper;

    private ImageService imageService;

    /**
     * Not properly mapped direction
     *
     * @param dto
     * @return
     */
    @Override
    public Book toEntity(BookDTO dto) {
        Book entity = modelMapper.map(dto, Book.class);
        return entity;
    }

    @Override
    public BookDTO toDto(Book entity) {
        BookDTO dto = modelMapper.map(entity, BookDTO.class);
        PublishingInfo publishingInfo = entity.getPublishingInfo();
        dto.setPrice(publishingInfo.getPrice());
        dto.setDiscount(publishingInfo.getDiscount());
        dto.setMerchantName(publishingInfo.getMerchant().getName());
        dto.setImageData(imageService.loadImageAsBase64(entity.getImage().getData()));
        return dto;
    }

    @Autowired
    public BookDtoMapper(ModelMapper modelMapper, ImageService imageService) {
        this.modelMapper = modelMapper;
        this.imageService = imageService;
    }
}
