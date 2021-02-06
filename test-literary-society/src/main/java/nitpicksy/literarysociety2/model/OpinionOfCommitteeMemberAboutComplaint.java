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
public class OpinionOfCommitteeMemberAboutComplaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PrimaryKeyJoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private User committeeMember;

    @Column
    private boolean isPlagiarism;

    @Column
    private boolean reviewed;

    @PrimaryKeyJoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private PlagiarismComplaint plagiarismComplaint;

    public OpinionOfCommitteeMemberAboutComplaint(User committeeMember, boolean isPlagiarism, PlagiarismComplaint plagiarismComplaint) {
        this.committeeMember = committeeMember;
        this.isPlagiarism = isPlagiarism;
        this.plagiarismComplaint = plagiarismComplaint;
    }

}
