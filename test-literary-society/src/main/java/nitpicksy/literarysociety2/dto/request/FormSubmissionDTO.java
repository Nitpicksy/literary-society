package nitpicksy.literarysociety2.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormSubmissionDTO implements Serializable {

    @NotEmpty(message =  "Field Id is empty.")
    private String fieldId;

    private String fieldValue;
}
