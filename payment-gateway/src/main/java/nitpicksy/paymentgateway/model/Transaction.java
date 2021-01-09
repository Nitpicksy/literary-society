package nitpicksy.paymentgateway.model;

import lombok.*;
import nitpicksy.paymentgateway.enumeration.TransactionStatus;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PrimaryKeyJoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private Merchant merchant;

    @PrimaryKeyJoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private Company company;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String merchantOrderId;

    @Column
    private Long paymentId;

    @Column(nullable = false)
    private Timestamp merchantTimestamp;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @PrimaryKeyJoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private PaymentMethod paymentMethod;

}
