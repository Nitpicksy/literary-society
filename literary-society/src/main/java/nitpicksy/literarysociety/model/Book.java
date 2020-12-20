package nitpicksy.literarysociety.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.literarysociety.enumeration.BookStatus;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @PrimaryKeyJoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private Writer writer;

    @Column(nullable = false)
    private String writersNames;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String synopsis;

    @PrimaryKeyJoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private Genre genre;

    @OneToOne(mappedBy = "book")
    private PublishingInfo publishingInfo;

    @PrimaryKeyJoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private User editor;

    @PrimaryKeyJoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private User lecturer;

    @PrimaryKeyJoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private Image image;

    public Book(Writer writer, String title, String synopsis, Genre genre, BookStatus status) {
        this.writer = writer;
        this.writersNames = String.format("%s %s", writer.getFirstName(), writer.getLastName());
        this.title = title;
        this.synopsis = synopsis;
        this.genre = genre;
        this.status = status;
    }
}
