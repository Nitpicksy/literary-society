package nitpicksy.literarysociety.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
public class PaymentResponseDTO {

    @NotBlank(message = "Payment id is empty")
    @Positive(message = "Payment id must be positive.")
    private Long paymentId;

    @NotBlank(message = "Payment URL is empty")
    @Pattern(regexp = "(http(s)?:\\/\\/)?((www\\.)|(localhost:))[(\\/)?a-zA-Z0-9@:%._\\+~#=-]{1,256}")
    private String paymentURL;
}