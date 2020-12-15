package nitpicksy.paymentgateway.dto.request;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.*;

import javax.validation.constraints.*;
import java.util.LinkedHashMap;
import java.util.Map;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DynamicPaymentDetailsDTO {

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 10, fraction = 2)
    private Double amount;

    @NotNull
    @Digits(integer = 10, fraction = 0)
    private Long merchantOrderId;

    @NotBlank(message = "Timestamp is not provided.")
    private String merchantTimestamp;

    @NotNull
    @Pattern(regexp = "((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9.-]+(:[0-9]+)?|(?:www.|[-;:&=\\+\\$,\\w]+@)[A-Za-z0-9.-]+)((?:\\/[\\+~%\\/.\\w-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[\\w]*))?)", message = "Success URL is invalid.")
    private String successURL;

    @NotNull
    @Pattern(regexp = "((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9.-]+(:[0-9]+)?|(?:www.|[-;:&=\\+\\$,\\w]+@)[A-Za-z0-9.-]+)((?:\\/[\\+~%\\/.\\w-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[\\w]*))?)", message = "Failed URL is invalid.")
    private String failedURL;

    @NotNull
    @Pattern(regexp = "((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9.-]+(:[0-9]+)?|(?:www.|[-;:&=\\+\\$,\\w]+@)[A-Za-z0-9.-]+)((?:\\/[\\+~%\\/.\\w-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[\\w]*))?)", message = "Error URL is invalid.")
    private String errorURL;

    Map<String, Object> paymentDetails = new LinkedHashMap<>();

    @JsonAnySetter
    public void setDetails(String key, Object value) {
        paymentDetails.put(key, value);
    }
}
