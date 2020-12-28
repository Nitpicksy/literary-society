package nitpicksy.paymentgateway.mapper;

import nitpicksy.paymentgateway.dto.request.CreatePaymentMethodMainDataDTO;
import nitpicksy.paymentgateway.model.PaymentMethod;
import org.springframework.stereotype.Component;

@Component
public class CreatePaymentMethodMainDataMapper implements MapperInterface<PaymentMethod, CreatePaymentMethodMainDataDTO> {

    @Override
    public PaymentMethod toEntity(CreatePaymentMethodMainDataDTO dto) {
        return new PaymentMethod(dto.getName(), dto.getCommonName(),dto.getApi(), dto.getSubscription(), dto.getEmail());
    }

    @Override
    public CreatePaymentMethodMainDataDTO toDto(PaymentMethod entity) {
        return null;
    }

}