package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.model.OpinionOfCommitteeMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommitteeOpinionRepository extends JpaRepository<OpinionOfCommitteeMember, Long> {
    List<OpinionOfCommitteeMember> findOpinionOfCommitteeMemberByWriterUsernameAndReviewed(String username, boolean reviewed);
}
