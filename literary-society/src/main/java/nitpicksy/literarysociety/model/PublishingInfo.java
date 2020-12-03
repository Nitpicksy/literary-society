package nitpicksy.literarysociety.model;

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

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private Book book;

    @PrimaryKeyJoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private Merchant merchant;
}
