package nitpicksy.literarysociety2.mapper;

import nitpicksy.literarysociety2.dto.response.MerchantResponseDTO;
import nitpicksy.literarysociety2.model.Merchant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MerchantResponseMapper implements MapperInterface<Merchant, MerchantResponseDTO> {

    private ModelMapper modelMapper;

    @Override
    public Merchant toEntity(MerchantResponseDTO dto) {
        Merchant entity = modelMapper.map(dto, Merchant.class);
        return entity;
    }

    @Override
    public MerchantResponseDTO toDto(Merchant entity) {
        MerchantResponseDTO merchantResponseDTO = new MerchantResponseDTO();
        merchantResponseDTO.setName(entity.getName());
        merchantResponseDTO.setCity(entity.getCity());
        merchantResponseDTO.setCountry(entity.getCountry());
        merchantResponseDTO.setEmail(entity.getEmail());
        merchantResponseDTO.setId(entity.getUserId());
        merchantResponseDTO.setStatus(entity.getStatus().toString());
        return merchantResponseDTO;
    }

    @Autowired
    public MerchantResponseMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}