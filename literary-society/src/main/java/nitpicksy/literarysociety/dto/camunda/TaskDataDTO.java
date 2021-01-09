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

    private PlagiarismDetailsDTO plagiarismDetails;

    private List<EditorsCommentsDTO> editorsComments;

    public TaskDataDTO(FormFieldsDTO formFieldsDTO, PublicationRequestDTO publicationRequestDTO) {
        this.formFieldsDTO = formFieldsDTO;
        this.publicationRequestDTO = publicationRequestDTO;
    }

    public TaskDataDTO(FormFieldsDTO formFieldsDTO, List<WriterDocumentDTO> writerDocumentDTO) {
        this.formFieldsDTO = formFieldsDTO;
        this.writerDocumentDTO = writerDocumentDTO;
    }

    public TaskDataDTO(FormFieldsDTO formFieldsDTO, PlagiarismDetailsDTO plagiarismDetails) {
        this.formFieldsDTO = formFieldsDTO;
        this.plagiarismDetails = plagiarismDetails;
    }

    public TaskDataDTO(FormFieldsDTO formFieldsDTO, PlagiarismDetailsDTO plagiarismDetails, List<EditorsCommentsDTO> editorsComments) {
        this.formFieldsDTO = formFieldsDTO;
        this.plagiarismDetails = plagiarismDetails;
        this.editorsComments = editorsComments;
    }
}
