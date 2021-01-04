package nitpicksy.paymentgateway.mapper;

import nitpicksy.paymentgateway.dto.response.PaymentMethodDataDTO;
import nitpicksy.paymentgateway.model.PaymentMethod;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class PaymentMethodDataMapper implements MapperInterface<PaymentMethod, PaymentMethodDataDTO> {

    private final ModelMapper modelMapper;

    private DataMapper dataMapper;

    @Override
    public PaymentMethod toEntity(PaymentMethodDataDTO dto) {
        return null;
    }

    @Override
    public PaymentMethodDataDTO toDto(PaymentMethod entity) {
        PaymentMethodDataDTO dto = modelMapper.map(entity, PaymentMethodDataDTO.class);
        dto.setApi(entity.getURI());
        dto.setListPaymentData(dataMapper.convertToDto(entity.getData()));
        return dto;
    }

    @Autowired
    public PaymentMethodDataMapper(ModelMapper modelMapper,DataMapper dataMapper) {
        this.modelMapper = modelMapper;
        this.dataMapper = dataMapper;
    }


}