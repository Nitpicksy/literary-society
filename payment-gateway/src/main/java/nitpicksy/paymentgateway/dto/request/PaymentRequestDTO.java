package nitpicksy.paymentgateway.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentRequestDTO {

    @NotNull
    @Positive
    private Long orderId;

    @NotBlank
    private String paymentCommonName;
}
