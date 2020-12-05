package nitpicksy.literarysociety.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("READER")
public class Reader extends User{

    @Column(nullable = false)
    private boolean isBetaReader;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "pucrhased_books",
            joinColumns = @JoinColumn(name = "reader_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"))
    private Set<Book> purchasedBooks = new HashSet<>();

    @Column
    private Integer penalty = 0;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "reader_genre",
            joinColumns = @JoinColumn(name = "reader_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id"))
    private Set<Genre> genre = new HashSet<>();
}
