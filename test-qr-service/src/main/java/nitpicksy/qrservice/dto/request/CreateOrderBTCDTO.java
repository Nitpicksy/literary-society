package nitpicksy.qrservice.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateOrderBTCDTO {

    String order_id;

    @NotNull
    Double price_amount;

    @NotNull
    String price_currency;

    @NotNull
    String receive_currency;

    String title;

    String description;

    String callback_url;

    String cancel_url;

    String success_url;

    String token;

    public CreateOrderBTCDTO(String order_id, Double price_amount, String price_currency, String receive_currency, String callback_url, String cancel_url, String success_url,
                             String token) {
        super();
        this.order_id = order_id;
        this.price_amount = price_amount;
        this.price_currency = price_currency;
        this.receive_currency = receive_currency;
        this.callback_url = callback_url;
        this.cancel_url = cancel_url;
        this.success_url = success_url;
        this.token = token;
    }

}
