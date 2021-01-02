package nitpicksy.paymentgateway.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.paymentgateway.dto.request.PaymentDataDTO;
import nitpicksy.paymentgateway.enumeration.PaymentMethodStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDataDTO {

    private Long id;

    private String name;

    private String api;

    private Boolean subscription;

    private String email;

    private PaymentMethodStatus status;

    private List<PaymentDataDTO> listPaymentData;

}
