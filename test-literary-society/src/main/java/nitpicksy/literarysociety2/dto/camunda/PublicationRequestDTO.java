package nitpicksy.literarysociety2.dto.camunda;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublicationRequestDTO {

    @Positive(message = "Id must be positive.")
    @NotNull(message = "Id is null.")
    private Long id;

    @NotBlank(message = "Title is empty.")
    private String title;

    @NotBlank(message = "Genre is empty.")
    private String genre;

    @NotBlank(message = "Synopsis is empty.")
    private String synopsis;
}
