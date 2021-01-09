package nitpicksy.literarysociety.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MerchantPaymentGatewayResponseDTO {

    private String name;

    private boolean supportsPaymentMethods;

}
