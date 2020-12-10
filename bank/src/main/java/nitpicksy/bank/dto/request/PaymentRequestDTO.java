package nitpicksy.bank.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
public class PaymentRequestDTO {

    @NotNull(message = "Merchant order id is empty")
    @Positive(message = "Merchant order id must be positive.")
    private Long merchantOrderId;

    @NotBlank(message = "Merchant timestamp is empty.")
    @Pattern(regexp = "[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9](:[0-9][0-9][.]?[0-9]*)?")
    private String merchantTimestamp;

    @NotNull(message = "Amount is null.")
    @Min(value = 1,message = "Amount must be greater than 0")
    private Double amount;

    @NotBlank(message = "Merchant id is empty")
    private String merchantId;

    @NotBlank(message = "Merchant password is empty")
    private String merchantPassword;

    @NotBlank(message = "Success URL is empty")
    @Pattern(regexp = "(http(s)?:\\/\\/)?((www\\.)|(localhost:))[(\\/)?a-zA-Z0-9@:%._\\+~#=-]{1,256}")
    private String successURL;

    @NotBlank(message = "Error URL is empty")
    @Pattern(regexp = "(http(s)?:\\/\\/)?((www\\.)|(localhost:))[(\\/)?a-zA-Z0-9@:%._\\+~#=-]{1,256}")
    private String errorURL;

    @NotBlank(message = "Failed URL is empty")
    @Pattern(regexp = "(http(s)?:\\/\\/)?((www\\.)|(localhost:))[(\\/)?a-zA-Z0-9@:%._\\+~#=-]{1,256}")
    private String failedURL;
}
