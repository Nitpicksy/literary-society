package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.model.OpinionOfCommitteeMemberAboutComplaint;
import nitpicksy.literarysociety.model.OpinionOfEditorAboutComplaint;
import nitpicksy.literarysociety.model.PlagiarismComplaint;
import nitpicksy.literarysociety.repository.OpinionOfCommitteeMemberAboutComplaintRepository;
import nitpicksy.literarysociety.repository.OpinionOfEditorAboutComplaintRepository;
import nitpicksy.literarysociety.repository.PlagiarismComplaintRepository;
import nitpicksy.literarysociety.service.OpinionOfCommitteeMemberAboutComplaintService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommitteePlagiarismFinalVerdictService implements JavaDelegate {

    private OpinionOfCommitteeMemberAboutComplaintRepository opinionOfCommitteeMemberAboutComplaintRepository;
    private OpinionOfEditorAboutComplaintRepository opinionOfEditorAboutComplaintRepository;
    private PlagiarismComplaintRepository plagiarismComplaintRepository;
    private OpinionOfCommitteeMemberAboutComplaintService opinionOfCommitteeMemberAboutComplaintService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        Long plagiarismId = Long.valueOf((String) execution.getVariable("plagiarismId"));
        PlagiarismComplaint complaint = plagiarismComplaintRepository.findById(plagiarismId).orElse(null);

        List<OpinionOfCommitteeMemberAboutComplaint> decisions = opinionOfCommitteeMemberAboutComplaintService.findUnreviewedOpinions(complaint.getId(), false);

        int positiveCounter = 0;
        int negativeCounter = 0;
        for (OpinionOfCommitteeMemberAboutComplaint decision : decisions) {
            if (decision.isPlagiarism()) {
                positiveCounter++;
            } else {
                negativeCounter++;
            }

            decision.setReviewed(true);
            opinionOfCommitteeMemberAboutComplaintRepository.save(decision);
        }

        if (positiveCounter == decisions.size()) {
            execution.setVariable("unanimous", true);
            execution.setVariable("plagiarism", true);
        } else if (negativeCounter == decisions.size()) {
            execution.setVariable("unanimous", true);
            execution.setVariable("plagiarism", false);
        } else {
            execution.setVariable("unanimous", false);
            execution.setVariable("plagiarism", false);
        }

        List<OpinionOfEditorAboutComplaint> opinions = opinionOfEditorAboutComplaintRepository.findByPlagiarismComplaintId(complaint.getId());
        for (OpinionOfEditorAboutComplaint opinion : opinions) {
            opinion.setReviewed(true);
            opinionOfEditorAboutComplaintRepository.save(opinion);
        }
    }

    @Autowired
    public CommitteePlagiarismFinalVerdictService(OpinionOfCommitteeMemberAboutComplaintRepository opinionOfCommitteeMemberAboutComplaintRepository, OpinionOfEditorAboutComplaintRepository opinionOfEditorAboutComplaintRepository, PlagiarismComplaintRepository plagiarismComplaintRepository, OpinionOfCommitteeMemberAboutComplaintService opinionOfCommitteeMemberAboutComplaintService) {
        this.opinionOfCommitteeMemberAboutComplaintRepository = opinionOfCommitteeMemberAboutComplaintRepository;
        this.opinionOfEditorAboutComplaintRepository = opinionOfEditorAboutComplaintRepository;
        this.plagiarismComplaintRepository = plagiarismComplaintRepository;
        this.opinionOfCommitteeMemberAboutComplaintService = opinionOfCommitteeMemberAboutComplaintService;
    }
}
