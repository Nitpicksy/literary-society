package nitpicksy.bank.dto.response;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.bank.enumeration.TransactionStatus;

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

    @Positive(message = "Acquirer order id must be positive.")
    private Long acquirerOrderId;

    private Timestamp acquirerTimestamp;

    @NotNull
    @Positive(message = "Payment id must be positive.")
    private Long paymentId;

    @NotBlank
    private String status;

    public TransactionResponseDTO(@NotBlank String merchantOrderId,
                                  @Positive(message = "Acquirer order id must be positive.") Long acquirerOrderId, Timestamp acquirerTimestamp,
                                  @NotNull @Positive(message = "Payment id must be positive.") Long paymentId,
                                  @NotBlank TransactionStatus status) {
        this.merchantOrderId = merchantOrderId;
        this.acquirerOrderId = acquirerOrderId;
        this.acquirerTimestamp = acquirerTimestamp;
        this.paymentId = paymentId;
        this.status = status.toString();
    }
}
