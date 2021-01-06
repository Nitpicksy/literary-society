package nitpicksy.literarysociety.mapper;

import nitpicksy.literarysociety.dto.request.MerchantRequestDTO;
import nitpicksy.literarysociety.model.Merchant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MerchantRequestMapper implements MapperInterface<Merchant, MerchantRequestDTO> {

    private ModelMapper modelMapper;

    private PasswordEncoder passwordEncoder;

    @Override
    public Merchant toEntity(MerchantRequestDTO dto) {
        Merchant entity = modelMapper.map(dto, Merchant.class);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        return entity;
    }

    @Override
    public MerchantRequestDTO toDto(Merchant entity) {
        return modelMapper.map(entity, MerchantRequestDTO.class);
    }

    @Autowired
    public MerchantRequestMapper(ModelMapper modelMapper,PasswordEncoder passwordEncoder) {
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }
}
