package nitpicksy.paymentgateway.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.paymentgateway.dto.both.PaymentMethodDTO;
import nitpicksy.paymentgateway.enumeration.CompanyStatus;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponseDTO {

    private Long id;

    private String companyName;

    private String websiteURL;

    private String email;

    private List<PaymentMethodDTO> supportedPaymentMethods;

    private CompanyStatus status;

}
