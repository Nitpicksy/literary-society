package nitpicksy.paymentgateway.mapper;

import nitpicksy.paymentgateway.dto.response.MerchantResponseDTO;
import nitpicksy.paymentgateway.model.Merchant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MerchantResponseMapper implements MapperInterface<Merchant, MerchantResponseDTO> {

    private final ModelMapper modelMapper;

    @Override
    public Merchant toEntity(MerchantResponseDTO dto) {
        return null;
    }

    @Override
    public MerchantResponseDTO toDto(Merchant entity) {
        return  modelMapper.map(entity, MerchantResponseDTO.class);
    }

    @Autowired
    public MerchantResponseMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}