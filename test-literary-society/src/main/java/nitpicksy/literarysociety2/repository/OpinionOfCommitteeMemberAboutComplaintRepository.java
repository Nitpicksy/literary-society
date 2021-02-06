package nitpicksy.literarysociety2.repository;

import nitpicksy.literarysociety2.model.OpinionOfCommitteeMemberAboutComplaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpinionOfCommitteeMemberAboutComplaintRepository extends JpaRepository<OpinionOfCommitteeMemberAboutComplaint, Long> {

    List<OpinionOfCommitteeMemberAboutComplaint> findOpinionOfCommitteeMemberAboutComplaintByPlagiarismComplaintIdAndReviewed(Long plagiarismId, boolean reviewed);
}
