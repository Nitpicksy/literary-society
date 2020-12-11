package nitpicksy.paymentgateway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmPaymentRequestDTO {

    private Long acquirerOrderId;

    private Timestamp acquirerTimestamp;

    @NotNull
    @Positive(message = "Payment id must be positive.")
    private Long paymentId;

    @NotBlank
    private String status;
}
