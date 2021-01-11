package nitpicksy.literarysociety.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@NoArgsConstructor
public class MembershipDTO {

    @Positive(message = "Id must be positive.")
    @NotNull(message = "Id is null.")
    private Long id;

    @NotBlank(message = "User is empty.")
    private String user;

    @NotNull(message = "Price is null.")
    @PositiveOrZero(message = "Price is not a positive number.")
    private Double price;

    @NotNull(message = "Expiration date is null.")
    private String expirationDate;

    @NotBlank
    private boolean isSubscribed;

    @NotBlank(message = "merchantName is empty.")
    private String merchantName;

}
