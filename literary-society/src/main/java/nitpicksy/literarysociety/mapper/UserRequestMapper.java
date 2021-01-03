package nitpicksy.literarysociety.mapper;

import nitpicksy.literarysociety.dto.request.UserRequestDTO;
import nitpicksy.literarysociety.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMapper implements MapperInterface<User, UserRequestDTO> {

    private ModelMapper modelMapper;

    private PasswordEncoder passwordEncoder;

    @Override
    public User toEntity(UserRequestDTO dto) {
        User entity = modelMapper.map(dto, User.class);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        return entity;
    }

    @Override
    public UserRequestDTO toDto(User entity) {
        return modelMapper.map(entity, UserRequestDTO.class);
    }

    @Autowired
    public UserRequestMapper(ModelMapper modelMapper,PasswordEncoder passwordEncoder) {
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }
}

