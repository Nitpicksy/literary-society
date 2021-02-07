package nitpicksy.literarysociety2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpinionDTO {

    private Long id;

    private String comment;

    private String commenterName;

}
