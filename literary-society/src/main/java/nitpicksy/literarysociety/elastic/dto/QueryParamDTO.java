package nitpicksy.literarysociety.elastic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.literarysociety.enumeration.BoolQueryType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryParamDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String value;

    @NotNull
    private Boolean isPhrase;

    @Pattern(regexp = "^(AND|OR)$")
    private BoolQueryType boolQueryType;

}
