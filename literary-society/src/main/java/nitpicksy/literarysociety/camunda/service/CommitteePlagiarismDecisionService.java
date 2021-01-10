package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety.enumeration.PlagiarismDecision;
import nitpicksy.literarysociety.model.OpinionOfCommitteeMemberAboutComplaint;
import nitpicksy.literarysociety.model.PlagiarismComplaint;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.repository.OpinionOfCommitteeMemberAboutComplaintRepository;
import nitpicksy.literarysociety.repository.PlagiarismComplaintRepository;
import nitpicksy.literarysociety.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommitteePlagiarismDecisionService implements JavaDelegate {

    private UserService userService;
    private OpinionOfCommitteeMemberAboutComplaintRepository opinionOfCommitteeMemberAboutComplaintRepository;
    private PlagiarismComplaintRepository plagiarismComplaintRepository;


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, String> map = formData.stream().collect(Collectors.toMap(FormSubmissionDTO::getFieldId, FormSubmissionDTO::getFieldValue));
        Long plagiarismId = Long.valueOf((String) execution.getVariable("plagiarismId"));

        String decision = map.get("decision");

        PlagiarismComplaint complaint = plagiarismComplaintRepository.findById(plagiarismId).orElse(null);

        String committeeUsername = (String) execution.getVariable("committeeMember");
        User committee = userService.findByUsername(committeeUsername);

        boolean plagiarismDecision;
        if ((decision).equals(PlagiarismDecision.PLAGIARISM.toString())) {
            plagiarismDecision = true;
        } else {
            plagiarismDecision = false;
        }

        OpinionOfCommitteeMemberAboutComplaint opinionOfCommitteeMemberAboutComplaint = new OpinionOfCommitteeMemberAboutComplaint(committee, plagiarismDecision, complaint);
        opinionOfCommitteeMemberAboutComplaintRepository.save(opinionOfCommitteeMemberAboutComplaint);
    }

    @Autowired
    public CommitteePlagiarismDecisionService(UserService userService, OpinionOfCommitteeMemberAboutComplaintRepository opinionOfCommitteeMemberAboutComplaintRepository, PlagiarismComplaintRepository plagiarismComplaintRepository) {
        this.userService = userService;
        this.opinionOfCommitteeMemberAboutComplaintRepository = opinionOfCommitteeMemberAboutComplaintRepository;
        this.plagiarismComplaintRepository = plagiarismComplaintRepository;
    }
}
