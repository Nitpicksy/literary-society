package nitpicksy.pcc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.pcc.enumeration.TransactionStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    private Long acquirerOrderId;

    @Column(nullable = false)
    private Timestamp acquirerTimestamp;

    @Column(nullable = false)
    private Long issuerOrderId;

    @Column(nullable = false)
    private Timestamp issuerTimestamp;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    public Transaction(Long acquirerOrderId, Timestamp acquirerTimestamp, Double amount) {
        this.acquirerOrderId = acquirerOrderId;
        this.acquirerTimestamp = acquirerTimestamp;
        this.amount = amount;
    }
}
