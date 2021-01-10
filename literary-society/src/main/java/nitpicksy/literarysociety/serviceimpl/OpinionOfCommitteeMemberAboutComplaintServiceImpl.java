package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.model.OpinionOfCommitteeMemberAboutComplaint;
import nitpicksy.literarysociety.repository.OpinionOfCommitteeMemberAboutComplaintRepository;
import nitpicksy.literarysociety.service.OpinionOfCommitteeMemberAboutComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpinionOfCommitteeMemberAboutComplaintServiceImpl implements OpinionOfCommitteeMemberAboutComplaintService {

    private OpinionOfCommitteeMemberAboutComplaintRepository opinionOfCommitteeMemberAboutComplaintRepository;

    @Override
    public List<OpinionOfCommitteeMemberAboutComplaint> findUnreviewedOpinions(Long plagiarismId, boolean reviewed) {
        return opinionOfCommitteeMemberAboutComplaintRepository.findOpinionOfCommitteeMemberAboutComplaintByPlagiarismComplaintIdAndReviewed(plagiarismId, reviewed);
    }

    @Autowired
    public OpinionOfCommitteeMemberAboutComplaintServiceImpl(OpinionOfCommitteeMemberAboutComplaintRepository opinionOfCommitteeMemberAboutComplaintRepository) {
        this.opinionOfCommitteeMemberAboutComplaintRepository = opinionOfCommitteeMemberAboutComplaintRepository;
    }
}
