package nitpicksy.literarysociety2.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JWTRequestDTO {

    @NotBlank
    private String jwtToken;

    @NotBlank
    private String refreshJwt;

    @NotNull
    private Date expirationDate;
}
