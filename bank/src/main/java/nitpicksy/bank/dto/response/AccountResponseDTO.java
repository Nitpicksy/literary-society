package nitpicksy.bank.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
public class AccountResponseDTO {

    @Positive
    private Long id;

    @NotBlank(message = "First Name is empty")
    private String firstName;

    @NotBlank(message = "Last Name is empty")
    private String lastName;

    @NotBlank(message = "City is empty")
    private String city;

    @NotBlank(message = "Country is empty")
    private String country;

    @NotBlank(message = "Email is empty")
    @Email
    private String email;

    @Positive
    @NotNull
    private Double balance;
}