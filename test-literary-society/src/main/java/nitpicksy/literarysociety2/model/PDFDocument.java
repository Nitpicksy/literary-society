package nitpicksy.literarysociety2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PDFDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private LocalDateTime created;

    @PrimaryKeyJoinColumn
    @ManyToOne
    private Book book;

    public PDFDocument(String name, LocalDateTime created, Book book) {
        this.name = name;
        this.created = created;
        this.book = book;
    }
    
}
