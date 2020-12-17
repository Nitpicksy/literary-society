package nitpicksy.literarysociety.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class BookDetailsDTO {

    @NotNull
    private BookDTO bookDTO;

    @NotBlank(message = "Synopsis is empty.")
    private String synopsis;

    @NotBlank(message = "Genre name is empty.")
    private String genreName;

    @NotBlank(message = "ISBN is empty.")
    private String ISBN;

    @NotBlank(message = "Publisher City is empty.")
    private String publisherCity;

    @NotBlank(message = "Publication Date is empty.")
    private String publicationDate;

    private String publisher;
}
