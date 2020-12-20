package nitpicksy.literarysociety.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.literarysociety.enumeration.BookStatus;
import nitpicksy.literarysociety.enumeration.TransactionStatus;
import nitpicksy.literarysociety.enumeration.TransactionType;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @PrimaryKeyJoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private User buyer;

    @Column(nullable = false)
    private Timestamp merchantTimestamp = new Timestamp(DateTime.now().getMillis());

    @Column(nullable = false)
    private Double amount;

    @ManyToMany( fetch = FetchType.LAZY)
    @JoinTable(name = "order_books",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"))
    private Set<Book> orderedBooks = new HashSet<>();

    @PrimaryKeyJoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private Merchant merchant;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "id")
    private Membership membership;

    public Transaction(TransactionStatus status, TransactionType type, User buyer, Double amount, Set<Book> orderedBooks, Merchant merchant) {
        this.status = status;
        this.type = type;
        this.buyer = buyer;
        this.amount = amount;
        this.orderedBooks = orderedBooks;
        this.merchant = merchant;
    }
}
