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

        List<OpinionOfCommitteeMember> opinionOfCommitteeMembers = committeeOpinionRepository.findOpinionOfCommitteeMemberByWriterUsername(username);

        execution.setVariable("moreDocuments", false);
        execution.setVariable("rejected", false);

        opinionOfCommitteeMembers.forEach(opinion -> {
            if (opinion.getOpinion().equals(CommitteeMemberOpinion.MORE_DOCUMENTS)) {
                execution.setVariable("moreDocuments", true);
            }
        });

        if (isRejected(opinionOfCommitteeMembers)) {
            execution.setVariable("rejected", true);
        }
    }

    private Boolean isRejected(List<OpinionOfCommitteeMember> opinionOfCommitteeMembers) {
        int counter = 0;

        for (OpinionOfCommitteeMember opinionOfCommitteeMember : opinionOfCommitteeMembers) {
            if (opinionOfCommitteeMember.getOpinion().equals(CommitteeMemberOpinion.DOES_NOT_MEETS_CRITERIA)) {
                counter++;
            }
        }
        return counter >= Math.round((double) opinionOfCommitteeMembers.size() / 2);
    }

    @Autowired
    public CommitteeVotingService(CommitteeOpinionRepository committeeOpinionRepository, WriterRepository writerRepository) {
        this.committeeOpinionRepository = committeeOpinionRepository;
        this.writerRepository = writerRepository;
    }
}
