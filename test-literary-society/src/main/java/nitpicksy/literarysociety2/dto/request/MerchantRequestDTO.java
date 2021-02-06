package nitpicksy.literarysociety2.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class MerchantRequestDTO {
    @NotBlank(message = "Name is empty.")
    private String name;

    @NotBlank(message = "City is empty.")
    private String city;

    @NotBlank(message = "Country is empty.")
    private String country;

    @Email
    @NotBlank(message = "Email is empty.")
    private String email;

    @NotBlank(message = "Username is empty.")
    private String username;

    @NotBlank(message = "New password is empty.")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[_#?!@$%^&*-.,:;]).{10,64}$", message = "Password must be between 10 and 64 characters long and must contain a number, a special character, a lowercase and an uppercase letter.")
    private String password;

    @NotBlank(message = "Repeated password is empty.")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[_#?!@$%^&*-.,:;]).{10,64}$", message = "Password must be between 10 and 64 characters long and must contain a number, a special character, a lowercase and an uppercase letter.")
    private String repeatedPassword;
}
