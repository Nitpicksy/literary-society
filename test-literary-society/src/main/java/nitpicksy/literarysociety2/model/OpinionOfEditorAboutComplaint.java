package nitpicksy.literarysociety2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpinionOfEditorAboutComplaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PrimaryKeyJoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private User editor;

    @Column(nullable = false, length = 1000)
    private String review;

    @PrimaryKeyJoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private PlagiarismComplaint plagiarismComplaint;

    public OpinionOfEditorAboutComplaint(User editor, String review, PlagiarismComplaint plagiarismComplaint) {
        this.editor = editor;
        this.review = review;
        this.plagiarismComplaint = plagiarismComplaint;
    }
}
