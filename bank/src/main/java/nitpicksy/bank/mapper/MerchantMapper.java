package nitpicksy.bank.mapper;

import nitpicksy.bank.dto.MerchantDTO;
import nitpicksy.bank.dto.request.PaymentRequestDTO;
import nitpicksy.bank.model.Merchant;
import nitpicksy.bank.model.PaymentRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MerchantMapper implements MapperInterface<Merchant, MerchantDTO> {

    private ModelMapper modelMapper;

    @Override
    public Merchant toEntity(MerchantDTO dto) {
        return modelMapper.map(dto, Merchant.class);
    }

    @Override
    public MerchantDTO toDto(Merchant entity) {
        return modelMapper.map(entity, MerchantDTO.class);
    }

    @Autowired
    public MerchantMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}