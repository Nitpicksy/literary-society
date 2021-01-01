package nitpicksy.literarysociety.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.literarysociety.enumeration.CommitteeMemberOpinion;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpinionOfCommitteeMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PrimaryKeyJoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private User committeeMember;

    @PrimaryKeyJoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private Writer writer;

    @Column(nullable = false)
    private String comment;

    @Enumerated(EnumType.STRING)
    private CommitteeMemberOpinion opinion;

    public OpinionOfCommitteeMember(User committeeMember, Writer writer, String comment, CommitteeMemberOpinion opinion) {
        this.committeeMember = committeeMember;
        this.writer = writer;
        this.comment = comment;
        this.opinion = opinion;
    }
}
