package nitpicksy.bank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmPaymentResponseDTO {

    @Positive(message = "Acquirer order id must be positive.")
    private Long acquirerOrderId;

    @NotBlank
    private String acquirerTimestamp;

    @NotNull
    @Positive(message = "Payment id must be positive.")
    private Long paymentId;

    @NotBlank
    private String status;

    public ConfirmPaymentResponseDTO(@NotNull @Positive(message = "Payment id must be positive.") Long paymentId, @NotBlank String status) {
        this.paymentId = paymentId;
        this.status = status;
        this.acquirerOrderId = null;
        this.acquirerTimestamp = null;
    }
}
