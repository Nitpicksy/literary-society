package nitpicksy.paymentgateway.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlanDTO {

    @Positive(message = "Id must be positive.")
    @NotNull(message = "Id is null.")
    private Long id;

    @NotBlank(message = "Merchant name is empty.")
    private String merchantName;

    @NotBlank(message = "Product name is empty.")
    private String productName;

    @NotBlank(message = "Product type is empty.")
    private String productType;

    @NotBlank(message = "Product category is empty.")
    private String productCategory;

    @NotBlank(message = "Plan name is empty.")
    private String planName;

    @NotBlank(message = "Plan description is empty.")
    private String planDescription;

    @NotNull(message = "Price is null.")
    @PositiveOrZero(message = "Price is not a positive number.")
    private Double price;

    @NotBlank(message = "Frequency unit is empty.")
    private String frequencyUnit;

    @NotNull(message = "Frequency count is null.")
    @PositiveOrZero(message = "Frequency count is not a positive number.")
    private Integer frequencyCount;

    @NotBlank(message = "Success URL is empty.")
    private String successURL;

    @NotBlank(message = "Cancel URL unit is empty.")
    private String cancelURL;

}
