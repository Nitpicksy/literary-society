package nitpicksy.bank.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class PaymentDetailsDTO {

    @NotBlank(message = "Merchant id is empty")
    private String merchantId;

    @NotBlank(message = "Merchant password is empty")
    private String merchantPassword;
}
