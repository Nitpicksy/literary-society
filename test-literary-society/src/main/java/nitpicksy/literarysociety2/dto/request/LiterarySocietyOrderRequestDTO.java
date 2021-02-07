package nitpicksy.literarysociety2.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LiterarySocietyOrderRequestDTO {

    @NotNull
    @Positive(message = "Id must be positive.")
    private Long merchantOrderId;

    @NotBlank
    private String status;
}
