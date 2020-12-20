package nitpicksy.pcc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.pcc.enumeration.TransactionStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayResponseDTO {

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
