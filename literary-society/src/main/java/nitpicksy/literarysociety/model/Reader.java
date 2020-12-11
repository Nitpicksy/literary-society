package nitpicksy.literarysociety.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("READER")
public class Reader extends User {

    @Column
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
    private Set<Genre> genres = new HashSet<>();

    public Reader(String firstName, String lastName, String city, String country, String email, String username,
                  String password, boolean isBetaReader) {
        super(firstName, lastName, city, country, email, username, password);
        this.isBetaReader = isBetaReader;
    }

}
