package nitpicksy.literarysociety.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@NoArgsConstructor
public class PriceListDTO {

    @Positive(message = "Id must be positive.")
    @NotNull(message = "Id is null.")
    private Long id;

    @NotNull(message = "Writer membership is null.")
    @PositiveOrZero(message = "Writer membership is not a positive number.")
    private Double membershipForWriter;

    @NotNull(message = "Reader membership is null.")
    @PositiveOrZero(message = "Reader membership is not a positive number.")
    private Double membershipForReader;

    @NotNull(message = "Start date is null.")
    private String startDate;
}
