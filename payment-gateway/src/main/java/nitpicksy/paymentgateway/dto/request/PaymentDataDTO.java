package nitpicksy.paymentgateway.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDataDTO {

    private Long id;

    @NotBlank(message = "Attribute name is empty")
    private String attributeName;

    @NotBlank(message = "Attribute type is empty")
    private String attributeType;

    @NotBlank(message = "Attribute JSON name is empty")
    private String attributeJSONName;
}
