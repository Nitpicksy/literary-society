package nitpicksy.paymentgateway.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.paymentgateway.dto.both.PaymentMethodDTO;
import nitpicksy.paymentgateway.dto.request.PaymentDataDTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDataResponseDTO {

    @NotNull
    private PaymentMethodDTO paymentMethodDTO;

    @NotEmpty
    private List<PaymentDataDTO> listPaymentDataDTO;
}
