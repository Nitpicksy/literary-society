package nitpicksy.paymentgateway.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.paymentgateway.dto.both.PaymentMethodDTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDataRequestDTO {

    @NotNull
    private PaymentMethodDTO paymentMethod;

    @NotEmpty
    private List<DataForPaymentRequestDTO> paymentData;
}
