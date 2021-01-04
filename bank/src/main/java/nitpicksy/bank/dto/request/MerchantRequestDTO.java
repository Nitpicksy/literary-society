package nitpicksy.bank.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
public class MerchantRequestDTO {

    @NotBlank(message = "Name is empty")
    private String name;

    @NotBlank(message = "Merchant password is empty")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[_#?!@$%^&*-.,:;]).{10,64}$", message = "Password must be between 10 and 64 characters long and must contain a number, a special character, a lowercase and an uppercase letter.")
    private String merchantPassword;

    @NotBlank(message = "City is empty")
    private String city;

    @NotBlank(message = "Country is empty")
    private String country;

    @NotBlank(message = "Email is empty")
    @Email
    private String email;

    @NotBlank(message = "Balance is empty")
    private String balance;
}
