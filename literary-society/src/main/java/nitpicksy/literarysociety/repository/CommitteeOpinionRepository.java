package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.model.OpinionOfCommitteeMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitteeOpinionRepository extends JpaRepository<OpinionOfCommitteeMember, Long> {
}
