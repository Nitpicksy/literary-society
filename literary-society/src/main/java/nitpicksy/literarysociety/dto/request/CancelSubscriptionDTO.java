package nitpicksy.literarysociety.dto.request;

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
public class CancelSubscriptionDTO {

    @Positive(message = "Plan id must be positive.")
    @NotNull(message = "Plan id is null.")
    private Long planId;

    @NotBlank(message = "Subscription id is empty.")
    private String subscriptionId;

}
