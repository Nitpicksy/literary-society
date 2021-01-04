package nitpicksy.literarysociety.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.literarysociety.enumeration.BookStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublicationRequestResponseDTO {

    private Long id;

    private String title;

    private String genre;

    private String synopsis;

    private String editorName;

    private BookStatus status;

}
