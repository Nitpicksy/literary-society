package nitpicksy.literarysociety2.dto.camunda;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlagiarismDetailsDTO {

    @NotBlank(message = "writerName is empty.")
    private String writerName;

    @NotBlank(message = "title is empty.")
    private String title;

    @NotBlank(message = "mainEditor is empty.")
    private String mainEditor;

    private PublicationRequestDTO bookDetails;

    public PlagiarismDetailsDTO(String writerName, String title, String mainEditor) {
        this.writerName = writerName;
        this.title = title;
        this.mainEditor = mainEditor;
    }
}
