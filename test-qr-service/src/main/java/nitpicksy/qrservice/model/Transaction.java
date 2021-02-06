package nitpicksy.qrservice.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.qrservice.enumeration.TransactionStatus;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String merchantOrderId;

    @Column(nullable = false)
    private Timestamp merchantTimestamp;

    @Column(nullable = false)
    private Long paymentId;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;


    public Transaction(Double amount, String merchantOrderId, Timestamp merchantTimestamp, Long paymentId) {
        this.amount = amount;
        this.merchantOrderId = merchantOrderId;
        this.merchantTimestamp = merchantTimestamp;
        this.paymentId = paymentId;
    }

    public Transaction(Double amount, String merchantOrderId, Timestamp merchantTimestamp, Long paymentId, TransactionStatus status) {
        this.amount = amount;
        this.merchantOrderId = merchantOrderId;
        this.merchantTimestamp = merchantTimestamp;
        this.paymentId = paymentId;
        this.status = status;
    }
}

