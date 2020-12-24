package nitpicksy.paymentgateway.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentMethodMainDataDTO {

    @NotBlank(message = "Name is empty")
    private String name;

    @NotNull
    @Pattern(regexp = "((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9.-]+(:[0-9]+)?|(?:www.|[-;:&=\\+\\$,\\w]+@)[A-Za-z0-9.-]+)((?:\\/[\\+~%\\/.\\w-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[\\w]*))?)", message = "Success URL is invalid.")
    private String api;

    @NotBlank(message = "Common name is empty")
    private String commonName;

    @NotNull
    private Boolean subscription;

    @NotBlank(message = "Email is empty")
    @Email
    private String email;
}
