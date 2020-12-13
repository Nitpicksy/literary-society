package nitpicksy.paymentgateway.model;

import lombok.*;
import nitpicksy.paymentgateway.enumeration.TransactionStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private Long merchantOrderId;

    @Column(nullable = false)
    private LocalDateTime merchantTimestamp;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

}
