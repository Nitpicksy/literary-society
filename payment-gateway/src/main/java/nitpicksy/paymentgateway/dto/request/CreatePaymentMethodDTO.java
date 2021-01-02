package nitpicksy.paymentgateway.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentMethodDTO {

    @NotNull
    private CreatePaymentMethodMainDataDTO mainData;

    private MultipartFile multipartFile;

    @NotEmpty
    private List<PaymentDataDTO> paymentData;
}
