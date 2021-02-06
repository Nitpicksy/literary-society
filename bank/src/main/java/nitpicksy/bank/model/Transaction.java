package nitpicksy.bank.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.bank.converter.CryptoStringConverter;
import nitpicksy.bank.enumeration.TransactionStatus;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Positive;
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
    @Convert(converter = CryptoStringConverter.class)
    private String merchantId;

    @Column(nullable = false)
    private String merchantOrderId;

    @Column(nullable = false)
    private Timestamp merchantTimestamp;

    @Column(nullable = false)
    private Long paymentId;

    @Column
    private String pan;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    public Transaction(Double amount, String merchantId, String merchantOrderId, Timestamp merchantTimestamp, String pan,Long paymentId) {
        this.amount = amount;
        this.merchantId = merchantId;
        this.merchantOrderId = merchantOrderId;
        this.merchantTimestamp = merchantTimestamp;
        this.pan = pan;
        this.paymentId = paymentId;
    }

    public Transaction(Double amount, String merchantId, String merchantOrderId, Timestamp merchantTimestamp, Long paymentId, String pan, TransactionStatus status) {
        this.amount = amount;
        this.merchantId = merchantId;
        this.merchantOrderId = merchantOrderId;
        this.merchantTimestamp = merchantTimestamp;
        this.paymentId = paymentId;
        this.pan = pan;
        this.status = status;
    }
}
