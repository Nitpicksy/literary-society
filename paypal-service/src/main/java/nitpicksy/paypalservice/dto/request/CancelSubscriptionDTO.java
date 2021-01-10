package nitpicksy.paypalservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CancelSubscriptionDTO {

    @NotBlank(message = "Plan id is empty.")
    private String planId;

    @NotBlank(message = "Subscription id is empty.")
    private String subscriptionId;

}
