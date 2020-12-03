package nitpicksy.paymentgateway.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.paymentgateway.enumeration.TransactionStatus;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    private Long merchantOrderId;

    @Column(nullable = false)
    private Long merchantTimestamp;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

}
