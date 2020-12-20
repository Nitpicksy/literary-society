package nitpicksy.paymentgateway.mapper;

import nitpicksy.paymentgateway.dto.response.PaymentMethodResponseDTO;
import nitpicksy.paymentgateway.model.PaymentMethod;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodMapper implements MapperInterface<PaymentMethod, PaymentMethodResponseDTO> {

    private final ModelMapper modelMapper;

    @Override
    public PaymentMethod toEntity(PaymentMethodResponseDTO dto) {
        return null;
    }

    @Override
    public PaymentMethodResponseDTO toDto(PaymentMethod entity) {
        return modelMapper.map(entity, PaymentMethodResponseDTO.class);
    }

    @Autowired
    public PaymentMethodMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


}
