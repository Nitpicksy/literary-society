package nitpicksy.literarysociety2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.camunda.bpm.engine.form.FormField;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormFieldsDTO {

    private String processInstanceId;

    private String taskId;

    private List<FormField> formFields;
}
