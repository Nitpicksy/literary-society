package nitpicksy.paypalservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmPaymentDTO {

    @NotBlank(message = "Payer id is empty.")
    private String payerId;

    @NotNull(message = "Payment details are null.")
    private PaymentDetailsDTO paymentDetails;

}
