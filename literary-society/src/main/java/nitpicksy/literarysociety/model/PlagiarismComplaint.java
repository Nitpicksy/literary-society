package nitpicksy.literarysociety.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlagiarismComplaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PrimaryKeyJoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private Writer writer;

    @Column(nullable = false)
    private String bookTitle;

    @ManyToMany( fetch = FetchType.LAZY)
    @JoinTable(name = "editor_plagiarism_complaint",
            joinColumns = @JoinColumn(name = "editor_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "plagiarism_complaint_id", referencedColumnName = "id"))
    private Set<User> editor = new HashSet<>();

    //Add column - status
}
