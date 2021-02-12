package nitpicksy.literarysociety.elasticsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.literarysociety.enumeration.SearchType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchParamDTO {

    @NotBlank
    private String attributeName;

    @NotBlank
    private String searchValue;

    @NotNull
    private Boolean phraseQuery;

    private SearchType type;
}
