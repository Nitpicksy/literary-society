package nitpicksy.literarysociety.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentGatewayPayRequestDTO {

    private Long transactionId;

    private String merchantName;

    private Double amount;
}
