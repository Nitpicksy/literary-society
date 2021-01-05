package nitpicksy.bank.mapper;

import nitpicksy.bank.dto.request.MerchantRequestDTO;
import nitpicksy.bank.model.Merchant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MerchantRequestMapper implements MapperInterface<Merchant, MerchantRequestDTO> {

    private ModelMapper modelMapper;

    @Override
    public Merchant toEntity(MerchantRequestDTO dto) {
        Merchant merchant = new Merchant();
        merchant.setName(dto.getName());
        merchant.setCity(dto.getCity());
        merchant.setCountry(dto.getCountry());
        merchant.setEmail(dto.getEmail());
        merchant.setBalance(Double.valueOf(dto.getBalance()));
        merchant.setMerchantPassword(dto.getMerchantPassword());
        return modelMapper.map(dto, Merchant.class);
    }

    @Override
    public MerchantRequestDTO toDto(Merchant entity) {
        return modelMapper.map(entity, MerchantRequestDTO.class);
    }

    @Autowired
    public MerchantRequestMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}