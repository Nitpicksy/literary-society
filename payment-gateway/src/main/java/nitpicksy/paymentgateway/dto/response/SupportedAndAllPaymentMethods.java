package nitpicksy.paymentgateway.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.paymentgateway.dto.both.PaymentMethodDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupportedAndAllPaymentMethods {

    private List<PaymentMethodDTO> paymentMethods;

    private List<PaymentMethodDTO> supportedMethods;
}
