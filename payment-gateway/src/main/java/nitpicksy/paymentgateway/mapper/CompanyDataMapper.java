package nitpicksy.paymentgateway.mapper;

import nitpicksy.paymentgateway.dto.request.CompanyDataDTO;
import nitpicksy.paymentgateway.dto.request.CreatePaymentMethodMainDataDTO;
import nitpicksy.paymentgateway.model.Company;
import nitpicksy.paymentgateway.model.PaymentMethod;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompanyDataMapper implements MapperInterface<Company, CompanyDataDTO> {

    private final ModelMapper modelMapper;

    @Override
    public Company toEntity(CompanyDataDTO dto) {
        Company entity = modelMapper.map(dto, Company.class);
        entity.setURI(dto.getWebsiteURL());
        return entity;
    }

    @Override
    public CompanyDataDTO toDto(Company entity) {
        CompanyDataDTO dto = modelMapper.map(entity, CompanyDataDTO.class);
        dto.setWebsiteURL(entity.getURI());
        return dto;
    }

    @Autowired
    public CompanyDataMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}