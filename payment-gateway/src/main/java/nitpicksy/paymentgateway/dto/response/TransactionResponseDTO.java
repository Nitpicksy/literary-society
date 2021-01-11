package nitpicksy.paymentgateway.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.paymentgateway.enumeration.TransactionStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class TransactionResponseDTO {

    @NotBlank
    private String merchantOrderId;

    @NotNull
    @Positive(message = "Payment id must be positive.")
    private Long paymentId;

    @NotBlank
    private String status;

    public TransactionResponseDTO(@NotBlank String merchantOrderId,
                                  @NotNull @Positive(message = "Payment id must be positive.") Long paymentId,
                                  @NotBlank TransactionStatus status) {
        this.merchantOrderId = merchantOrderId;
        this.paymentId = paymentId;
        this.status = status.toString();
    }
}

