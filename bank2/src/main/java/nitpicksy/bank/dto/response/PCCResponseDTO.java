package nitpicksy.bank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.bank.enumeration.TransactionStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PCCResponseDTO {

    @NotNull
    @Positive
    private Long acquirerOrderId;

    private Timestamp acquirerTimestamp;

    @NotNull
    @Positive
    private Long issuerOrderId;

    private Timestamp issuerTimestamp;

    private TransactionStatus status;

}
