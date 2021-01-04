package nitpicksy.literarysociety.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.text.RandomStringGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublishingInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ISBN;

    @Column(nullable = false)
    private Integer numberOfPages;

    @Column(nullable = false)
    private String publisherCity;

    @Column(nullable = false)
    private LocalDate publicationDate;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer discount;

    @OneToOne
    private Book book;

    @PrimaryKeyJoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private Merchant merchant;

    public PublishingInfo(Integer numberOfPages, String publisherCity, String publisher, Double price, Integer discount, Book book, Merchant merchant) {
        this.numberOfPages = numberOfPages;
        this.publisherCity = publisherCity;
        this.publisher = publisher;
        this.price = price;
        this.discount = discount;
        this.book = book;
        this.merchant = merchant;
        this.publicationDate = LocalDate.now();
        this.ISBN = generateISBN();
    }

    private String generateISBN() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', '9').build();
        return generator.generate(13);
    }
}
