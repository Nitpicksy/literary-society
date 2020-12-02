package nitpicksy.literarysociety.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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

    @Column(unique = true, nullable = false)
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
}
