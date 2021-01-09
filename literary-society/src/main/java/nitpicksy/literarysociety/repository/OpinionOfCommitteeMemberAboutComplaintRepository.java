package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.model.OpinionOfCommitteeMemberAboutComplaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpinionOfCommitteeMemberAboutComplaintRepository extends JpaRepository<OpinionOfCommitteeMemberAboutComplaint, Long> {

    List<OpinionOfCommitteeMemberAboutComplaint> findOpinionOfCommitteeMemberAboutComplaintByPlagiarismComplaintIdAndReviewed(Long plagiarismId, boolean reviewed);
}
