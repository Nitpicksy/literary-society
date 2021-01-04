package nitpicksy.paymentgateway.mapper;

import nitpicksy.paymentgateway.dto.response.PaymentDataResponseDTO;
import nitpicksy.paymentgateway.model.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentDataResponseMapper implements MapperInterface<PaymentMethod, PaymentDataResponseDTO>{

    private PaymentMethodDtoMapper paymentMethodDtoMapper;

    private DataMapper dataMapper;

    @Override
    public PaymentMethod toEntity(PaymentDataResponseDTO dto) {
        return null;
    }

    @Override
    public PaymentDataResponseDTO toDto(PaymentMethod entity) {
        PaymentDataResponseDTO paymentDataResponseDTO = new PaymentDataResponseDTO();
        paymentDataResponseDTO.setPaymentMethodDTO(paymentMethodDtoMapper.toDto(entity));
        paymentDataResponseDTO.setListPaymentDataDTO(dataMapper.convertToDto(entity.getData()));

        return paymentDataResponseDTO;
    }

    public List<PaymentDataResponseDTO> convert(List<PaymentMethod> paymentMethodList){
        List<PaymentDataResponseDTO> paymentDataResponseDTOS = new ArrayList<>();
        for (PaymentMethod paymentMethod:paymentMethodList) {
            paymentDataResponseDTOS.add(toDto(paymentMethod));
        }
        return paymentDataResponseDTOS;
    }
    @Autowired
    public PaymentDataResponseMapper(PaymentMethodDtoMapper paymentMethodDtoMapper,DataMapper dataMapper) {
        this.paymentMethodDtoMapper = paymentMethodDtoMapper;
        this.dataMapper = dataMapper;
    }
}
