package nitpicksy.literarysociety2.model;

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
@DiscriminatorValue("WRITER")
public class Writer extends User {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "writer_genre",
            joinColumns = @JoinColumn(name = "writer_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id"))
    private Set<Genre> genre = new HashSet<>();

    @Column
    private Integer attempts = 0;

    @OneToMany
    @JoinColumn(name = "writer_id")
    private Set<PDFDocument> drafts;


    public Writer(String firstName, String lastName, String city, String country, String email, String username,
                  String password) {
        super(firstName, lastName, city, country, email, username, password);
    }
}
