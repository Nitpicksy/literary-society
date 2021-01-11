package nitpicksy.bitcoinservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCallbackDTO {

    private Long id;

    private String order_id;

    private String status;

    private String price_amount;

    private String price_currency;

    private String receive_currency;

    private String receive_amount;

    private String pay_amount;

    private String pay_currency;

    private String underpaid_amount;

    private String overpaid_amount;

    private boolean is_refundable;

    private String created_at;

    private String token;

    private String error_message;
}
