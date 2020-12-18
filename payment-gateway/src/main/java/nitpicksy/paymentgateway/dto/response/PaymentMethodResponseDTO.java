package nitpicksy.paymentgateway.dto.response;

import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentMethodResponseDTO {

    @NotNull
    @Positive
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String commonName;

    @NotBlank
    private String URI;

    private boolean subscription;
}
