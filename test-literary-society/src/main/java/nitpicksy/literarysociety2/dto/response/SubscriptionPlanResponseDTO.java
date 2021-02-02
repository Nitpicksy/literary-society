package nitpicksy.literarysociety2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlanResponseDTO {

    private Long id;

    private String productName;

    private String planName;

    private String planDescription;

    private Double price;

    private String frequencyUnit;

    private Integer frequencyCount;

    private String membershipStatus;

}
