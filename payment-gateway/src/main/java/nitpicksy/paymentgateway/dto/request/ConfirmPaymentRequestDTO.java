package nitpicksy.paymentgateway.dto.request;

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

    private String merchantOrderId;

    private Timestamp acquirerTimestamp;

    @NotNull
    @Positive(message = "Payment id must be positive.")
    private Long paymentId;

    @NotBlank
    private String status;
}
