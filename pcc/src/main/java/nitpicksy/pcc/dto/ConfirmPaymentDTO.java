package nitpicksy.pcc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmPaymentDTO {

    @NotBlank(message = "PAN number is empty.")
    @Pattern(regexp = "^[0-9]{16}$")
    private String PAN;

    @NotBlank(message = "Card Holder Name is empty.")
    @Pattern(regexp = "^(([A-Za-zÀ-ƒ]+[.]?[ ]?|[a-zÀ-ƒ]+['-]?){0,4})$")
    private String cardHolderName;

    //month/year
    @NotBlank(message = "Expiration date is empty")
    @Pattern(regexp = "^([01]?[0-9]?(\\/)[0-9]{2})$")
    private String expirationDate;

    @NotBlank(message = "Security Code is empty.")
    @Pattern(regexp = "^[0-9]{3,4}$")
    private String securityCode;
}
