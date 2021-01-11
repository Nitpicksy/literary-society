package nitpicksy.literarysociety.mapper;

import nitpicksy.literarysociety.dto.response.PriceListDTO;
import nitpicksy.literarysociety.model.PriceList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PriceListDtoMapper implements MapperInterface<PriceList, PriceListDTO> {

    private ModelMapper modelMapper;


    @Override
    public PriceList toEntity(PriceListDTO dto) {
        return null;
    }

    @Override
    public PriceListDTO toDto(PriceList entity) {
        PriceListDTO dto = modelMapper.map(entity, PriceListDTO.class);
        dto.setStartDate(entity.getStartDate().toString());
        return dto;
    }
    
    @Autowired
    public PriceListDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

}
