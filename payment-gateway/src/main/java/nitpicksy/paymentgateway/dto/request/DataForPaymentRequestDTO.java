package nitpicksy.paymentgateway.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataForPaymentRequestDTO {

    @NotNull(message = "Id is null")
    @Positive
    private Long paymentDataId;

    @NotBlank(message = "Value is empty")
    private String attributeValue;
}
