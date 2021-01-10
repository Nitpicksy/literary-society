package nitpicksy.qrservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.qrservice.enumeration.PaymentStatus;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long paymentId;

    @Column(nullable = false)
    private String merchantOrderId;

    @Column(nullable = false)
    private String merchantToken;

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

    @Column
    private String creationDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column
    private Double receivedAmount;

    @Column
    private String receiveCurrency;
}
