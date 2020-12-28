package nitpicksy.paymentgateway.mapper;

import nitpicksy.paymentgateway.dto.both.PaymentMethodDTO;
import nitpicksy.paymentgateway.dto.response.PaymentMethodResponseDTO;
import nitpicksy.paymentgateway.model.PaymentMethod;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodDtoMapper implements MapperInterface<PaymentMethod, PaymentMethodDTO> {

    private final ModelMapper modelMapper;

    @Override
    public PaymentMethod toEntity(PaymentMethodDTO dto) {
        return modelMapper.map(dto, PaymentMethod.class);
    }

    @Override
    public PaymentMethodDTO toDto(PaymentMethod entity) {
        return modelMapper.map(entity, PaymentMethodDTO.class);
    }

    @Autowired
    public PaymentMethodDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


}
