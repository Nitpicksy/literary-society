package nitpicksy.literarysociety.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BuyerToken {

    private static final int BUYER_TOKEN_EXPIRY_TIME = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdDateTime;

    @Column(nullable = false)
    private LocalDateTime expiryDateTime;

    @OneToOne(targetEntity = Transaction.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "transaction_id")
    private Transaction transaction;

    public BuyerToken(Transaction transaction) {
        this.token = UUID.randomUUID().toString();
        this.transaction = transaction;
        this.createdDateTime = LocalDateTime.now();
        this.expiryDateTime = createdDateTime.plusMinutes(BUYER_TOKEN_EXPIRY_TIME);
    }
}