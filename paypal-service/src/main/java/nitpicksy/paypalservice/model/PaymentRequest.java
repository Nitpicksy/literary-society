package nitpicksy.paypalservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String paymentId;

    @Column(nullable = false)
    private String merchantClientId;

    @Column(nullable = false)
    private String merchantClientSecret;

    @Column(nullable = false)
    private String merchantOrderId;

    @Column(nullable = false)
    private Timestamp merchantTimestamp;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String successURL;

    @Column(nullable = false)
    private String failedURL;

    @Column(nullable = false)
    private String errorURL;
}
