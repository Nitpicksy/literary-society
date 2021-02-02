package nitpicksy.literarysociety2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PrimaryKeyJoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Column(nullable = false)
    private Double price;

    @Column
    private LocalDate expirationDate;

    @Column
    private boolean isSubscribed;

    @Column
    private String subscriptionId;

    @PrimaryKeyJoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private Merchant merchant;

    public Membership(User user, Double price, LocalDate expirationDate, boolean isSubscribed, Merchant merchant) {
        this.user = user;
        this.price = price;
        this.expirationDate = expirationDate;
        this.isSubscribed = isSubscribed;
        this.merchant = merchant;
    }
}
