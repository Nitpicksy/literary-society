package nitpicksy.bank.mapper;

import nitpicksy.bank.dto.response.MerchantResponseDTO;
import nitpicksy.bank.model.Merchant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MerchantResponseMapper implements MapperInterface<Merchant, MerchantResponseDTO> {

    private ModelMapper modelMapper;

    @Override
    public Merchant toEntity(MerchantResponseDTO dto) {
        return modelMapper.map(dto, Merchant.class);
    }

    @Override
    public MerchantResponseDTO toDto(Merchant entity) {
        return modelMapper.map(entity, MerchantResponseDTO.class);
    }

    @Autowired
    public MerchantResponseMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}