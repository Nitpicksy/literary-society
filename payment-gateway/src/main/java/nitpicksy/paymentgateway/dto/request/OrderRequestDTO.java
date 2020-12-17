package nitpicksy.paymentgateway.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    //    type: Number(10)
    @NotNull
    @Digits(integer = 10, fraction = 0)
    private Long orderId;

    //    type: Decimal(10,2)
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 10, fraction = 2)
    private Double amount;

    @NotBlank(message = "Timestamp is not provided.")
    private String timestamp;

    @NotNull
    @Pattern(regexp = "^(([A-Za-zÀ-ƒ]+[ ]?|[a-zÀ-ƒ]+['-]?){0,30})$", message = "Name is not valid.")
    private String merchantName;

}