package nitpicksy.paymentgateway.dto.request;

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
public class SubscriptionPlanToPaypalDTO {

    private String merchantClientId;

    private String merchantClientSecret;

    private String productName;

    private String productType;

    private String productCategory;

    private String planName;

    private String planDescription;

    private Double price;

    private String frequencyUnit;

    private Integer frequencyCount;

    private String successURL;

    private String cancelURL;

}
