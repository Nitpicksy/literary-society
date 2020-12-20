package nitpicksy.paypalservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailsDTO {

    @NotBlank(message = "Merchant's Client Id is empty")
    private String merchantClientId;

    @NotBlank(message = "Merchant's Client Secret is empty")
    private String merchantClientSecret;

}
