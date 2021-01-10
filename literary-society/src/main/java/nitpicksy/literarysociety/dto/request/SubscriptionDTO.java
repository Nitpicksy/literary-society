package nitpicksy.literarysociety.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDTO {

    @Positive(message = "Plan id must be positive.")
    @NotNull(message = "Plan id is null.")
    private Long planId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

}
