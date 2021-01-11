package nitpicksy.literarysociety.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentsDTO {

    @NotBlank(message = "Username is null")
    private String committeeUsername;

    @NotBlank(message = "Comment is null")
    private String comment;
}
