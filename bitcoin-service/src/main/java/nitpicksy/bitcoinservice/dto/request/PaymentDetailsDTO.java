package nitpicksy.bitcoinservice.dto.request;

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

    @NotBlank(message = "Merchant token is empty")
    private String merchantToken;

}
