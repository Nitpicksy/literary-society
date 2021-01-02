package nitpicksy.literarysociety.mapper;

import nitpicksy.literarysociety.dto.UserDTO;
import nitpicksy.literarysociety.dto.response.BookDTO;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.PublishingInfo;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.service.ImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements MapperInterface<User, UserDTO> {

    private ModelMapper modelMapper;

    @Override
    public User toEntity(UserDTO dto) {
        User entity = modelMapper.map(dto, User.class);
        return entity;
    }

    @Override
    public UserDTO toDto(User entity) {
        return modelMapper.map(entity, UserDTO.class);
    }

    @Autowired
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}

