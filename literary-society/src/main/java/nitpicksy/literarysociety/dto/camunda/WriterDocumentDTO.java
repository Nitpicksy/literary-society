package nitpicksy.literarysociety.dto.camunda;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WriterDocumentDTO {

    @NotBlank(message = "encodedData is empty.")
    String encodedData;

    @NotBlank(message = "name is empty.")
    String name;

    @NotBlank(message = "attempts are empty.")
    Integer attempts;

    @NotBlank(message = "writerFirstName are empty.")
    String writerFirstname;

    @NotBlank(message = "writerLastname are empty.")
    String writerLastname;

    @NotBlank(message = "writerUsername are empty.")
    String writerUsername;


}
