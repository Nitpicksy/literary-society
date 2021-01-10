package nitpicksy.bank.dto.request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PCCRequestDTO {

    @NotNull
    @Positive
    private Long acquirerOrderId;

    private Timestamp acquirerTimestamp;

    @NotNull(message = "Amount is null.")
    @Min(value = 1,message = "Amount must be greater than 0")
    private Double amount;

    @NotNull
    private ConfirmPaymentDTO confirmPaymentDTO;

    @NotBlank
    private String iin;

    @NotBlank
    private String merchantId;

    @NotBlank
    private String merchantOrderId;

    private Timestamp merchantTimestamp;

    @NotNull
    @Positive
    private Long paymentId;
}
