package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.enumeration.CommitteeMemberOpinion;
import nitpicksy.literarysociety.model.OpinionOfCommitteeMember;
import nitpicksy.literarysociety.model.Writer;
import nitpicksy.literarysociety.repository.CommitteeOpinionRepository;
import nitpicksy.literarysociety.repository.WriterRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommitteeVotingService implements JavaDelegate {

    private CommitteeOpinionRepository committeeOpinionRepository;
    private WriterRepository writerRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        String username = (String) execution.getVariable("writer");
        Writer writer = writerRepository.findByUsername(username);
        execution.setVariable("attempts", writer.getAttempts());

        List<OpinionOfCommitteeMember> opinionOfCommitteeMembers = committeeOpinionRepository.findOpinionOfCommitteeMemberByWriterUsernameAndReviewed(username, false);

        execution.setVariable("moreDocuments", false);
        execution.setVariable("rejected", false);

        handleOpinions(opinionOfCommitteeMembers, execution);
    }

    private void handleOpinions(List<OpinionOfCommitteeMember> opinionOfCommitteeMembers, DelegateExecution execution) {
        int counter = 0;

        for (OpinionOfCommitteeMember opinionOfCommitteeMember : opinionOfCommitteeMembers) {

            if (opinionOfCommitteeMember.getOpinion().equals(CommitteeMemberOpinion.DOES_NOT_MEETS_CRITERIA)) {
                counter++;
            }

            if (opinionOfCommitteeMember.getOpinion().equals(CommitteeMemberOpinion.MORE_DOCUMENTS)) {
                execution.setVariable("moreDocuments", true);
            }

            opinionOfCommitteeMember.setReviewed(true);
            committeeOpinionRepository.save(opinionOfCommitteeMember);
        }

        if (counter >= Math.round((double) opinionOfCommitteeMembers.size() / 2)) {
            execution.setVariable("rejected", true);
        }
    }

    @Autowired
    public CommitteeVotingService(CommitteeOpinionRepository committeeOpinionRepository, WriterRepository writerRepository) {
        this.committeeOpinionRepository = committeeOpinionRepository;
        this.writerRepository = writerRepository;
    }
}
