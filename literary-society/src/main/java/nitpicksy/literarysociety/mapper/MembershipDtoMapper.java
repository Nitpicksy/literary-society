package nitpicksy.literarysociety.mapper;

import nitpicksy.literarysociety.dto.response.MembershipDTO;
import nitpicksy.literarysociety.model.Membership;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MembershipDtoMapper implements MapperInterface<Membership, MembershipDTO> {

    private ModelMapper modelMapper;

    @Override
    public Membership toEntity(MembershipDTO dto) {
        return null;
    }

    @Override
    public MembershipDTO toDto(Membership entity) {
        MembershipDTO dto = modelMapper.map(entity, MembershipDTO.class);
        dto.setExpirationDate(entity.getExpirationDate().toString());
        dto.setUser(entity.getUser().getUsername());
        dto.setMerchantName(entity.getMerchant().getName());
        return dto;
    }

    @Autowired
    public MembershipDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
