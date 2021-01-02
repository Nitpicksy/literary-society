package nitpicksy.paymentgateway.dto.both;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.paymentgateway.dto.request.PaymentDataDTO;
import nitpicksy.paymentgateway.enumeration.PaymentMethodStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDTO {

    @NotNull(message = "Id is null")
    @Positive
    private Long id;

    @NotBlank(message = "Name is empty")
    private String name;

}
