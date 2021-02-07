package nitpicksy.literarysociety2.security;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthenticationRequest {
    @NotEmpty(message = "Username is empty")
    private String username;

    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[_#?!@$%^&*-.,:;]).{10,64}$", message = "Password must be between 10 and 64 characters long and must contain a number, a special character, a lowercase and an uppercase letter.")
    private String password;

}
