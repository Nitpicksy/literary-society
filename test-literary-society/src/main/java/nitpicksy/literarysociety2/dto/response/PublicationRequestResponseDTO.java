package nitpicksy.literarysociety2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.literarysociety2.enumeration.BookStatus;

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
