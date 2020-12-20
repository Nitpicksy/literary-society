package nitpicksy.literarysociety.dto.camunda;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.literarysociety.dto.response.FormFieldsDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDataDTO {

    private FormFieldsDTO formFieldsDTO;

    private PublicationRequestDTO publicationRequestDTO;
}
