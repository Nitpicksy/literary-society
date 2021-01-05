package nitpicksy.literarysociety.mapper;

import nitpicksy.literarysociety.dto.response.UserResponseDTO;
import nitpicksy.literarysociety.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserResponseMapper implements MapperInterface<User, UserResponseDTO> {

    private ModelMapper modelMapper;

    @Override
    public User toEntity(UserResponseDTO dto) {
        User entity = modelMapper.map(dto, User.class);
        return entity;
    }

    @Override
    public UserResponseDTO toDto(User entity) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(entity.getUserId());
        dto.setCity(entity.getCity());
        dto.setCountry(entity.getCountry());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        String roleName = entity.getRole().getName().substring(5);
        dto.setRole(roleName);
        dto.setStatus(entity.getStatus().toString());
        return dto;
    }

    @Autowired
    public UserResponseMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}