package nitpicksy.literarysociety2.dto.request;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class RequestTokenDTO {

    @NotBlank(message = "Username is empty.")
    private String username;

}

