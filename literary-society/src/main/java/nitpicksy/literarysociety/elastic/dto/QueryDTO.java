package nitpicksy.literarysociety.elastic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryDTO {

    private List<QueryParamDTO> queryParams;

    private String searchAllParam;

    @Positive
    @NotNull
    private Integer pageNum;

    @Positive
    @NotNull
    private Integer pageSize;

}
