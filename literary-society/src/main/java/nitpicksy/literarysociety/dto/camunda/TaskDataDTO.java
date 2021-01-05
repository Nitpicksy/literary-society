package nitpicksy.literarysociety.dto.camunda;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.literarysociety.dto.response.FormFieldsDTO;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDataDTO {

    private FormFieldsDTO formFieldsDTO;

    private PublicationRequestDTO publicationRequestDTO;

    private List<WriterDocumentDTO> writerDocumentDTO = new ArrayList<>();

    public TaskDataDTO(FormFieldsDTO formFieldsDTO, PublicationRequestDTO publicationRequestDTO) {
        this.formFieldsDTO = formFieldsDTO;
        this.publicationRequestDTO = publicationRequestDTO;
    }

    public TaskDataDTO(FormFieldsDTO formFieldsDTO, List<WriterDocumentDTO> writerDocumentDTO) {
        this.formFieldsDTO = formFieldsDTO;
        this.writerDocumentDTO = writerDocumentDTO;
    }
}
