package nitpicksy.paymentgateway.mapper;

import nitpicksy.paymentgateway.dto.both.PaymentMethodDTO;
import nitpicksy.paymentgateway.dto.request.CompanyDataDTO;
import nitpicksy.paymentgateway.dto.response.CompanyResponseDTO;
import nitpicksy.paymentgateway.model.Company;
import nitpicksy.paymentgateway.model.PaymentMethod;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CompanyResponseMapper implements MapperInterface<Company, CompanyResponseDTO> {

    private final ModelMapper modelMapper;

    private PaymentMethodDtoMapper paymentMethodDtoMapper;

    @Override
    public Company toEntity(CompanyResponseDTO dto) {
        Company entity = modelMapper.map(dto, Company.class);
        entity.setURI(dto.getWebsiteURL());
        Set<PaymentMethod> paymentMethods = dto.getSupportedPaymentMethods().stream()
                .map(method -> paymentMethodDtoMapper.toEntity(method))
                .collect(Collectors.toSet());
        entity.setPaymentMethods(paymentMethods);
        return entity;
    }

    @Override
    public CompanyResponseDTO toDto(Company entity) {
        CompanyResponseDTO dto = modelMapper.map(entity, CompanyResponseDTO.class);
        dto.setWebsiteURL(entity.getURI());
        List<PaymentMethodDTO> paymentMethods = entity.getPaymentMethods().stream()
                .map(method -> paymentMethodDtoMapper.toDto(method))
                .collect(Collectors.toList());
        dto.setSupportedPaymentMethods(paymentMethods);
        return dto;
    }

    @Autowired

    public CompanyResponseMapper(ModelMapper modelMapper, PaymentMethodDtoMapper paymentMethodDtoMapper) {
        this.modelMapper = modelMapper;
        this.paymentMethodDtoMapper = paymentMethodDtoMapper;
    }
}