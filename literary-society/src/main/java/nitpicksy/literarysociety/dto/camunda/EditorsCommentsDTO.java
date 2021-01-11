package nitpicksy.literarysociety.dto.camunda;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditorsCommentsDTO {

    private Long id;

    private String review;

    private String editorUsername;

    private String editorName;

}
