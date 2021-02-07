package nitpicksy.literarysociety2.serviceimpl;

import nitpicksy.literarysociety2.dto.request.CommitteeOpinionDTO;
import nitpicksy.literarysociety2.dto.response.CommentsDTO;
import nitpicksy.literarysociety2.enumeration.CommitteeMemberOpinion;
import nitpicksy.literarysociety2.exceptionHandler.InvalidDataException;
import nitpicksy.literarysociety2.model.Log;
import nitpicksy.literarysociety2.model.OpinionOfCommitteeMember;
import nitpicksy.literarysociety2.model.User;
import nitpicksy.literarysociety2.model.Writer;
import nitpicksy.literarysociety2.repository.CommitteeOpinionRepository;
import nitpicksy.literarysociety2.repository.WriterRepository;
import nitpicksy.literarysociety2.service.CommitteeOpinionService;
import nitpicksy.literarysociety2.service.LogService;
import nitpicksy.literarysociety2.service.UserService;
import nitpicksy.literarysociety2.utils.IPAddressProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommitteeOpinionServiceImpl implements CommitteeOpinionService {

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private CommitteeOpinionRepository committeeOpinionRepository;
    private WriterRepository writerRepository;
    private UserService userService;

    private LogService logService;

    private IPAddressProvider ipAddressProvider;

    @Override
    public void save(CommitteeOpinionDTO dto) {
        Writer writer = writerRepository.findByUsername(dto.getWriter());
        if (writer == null) {
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CO",
                    String.format("Writer %s doesn't exist",dto.getWriter())));
            throw new InvalidDataException("Writer is null", HttpStatus.BAD_REQUEST);
        }

        User authenticated = userService.getAuthenticatedUser();
        OpinionOfCommitteeMember opinion = new OpinionOfCommitteeMember(
                authenticated,
                writer,
                dto.getComment() != null ? dto.getComment() : "",
                CommitteeMemberOpinion.valueOf(dto.getOpinion()));
        OpinionOfCommitteeMember opinionOfCommitteeMember = committeeOpinionRepository.save(opinion);

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CO",
                String.format("Opinion of CommitteeMember %s successfully created from IP address %s",opinionOfCommitteeMember.getId(), ipAddressProvider.get())));
    }

    @Override
    public List<CommentsDTO> getWriterComments() {
        User user = userService.getAuthenticatedUser();
        List<CommentsDTO> commentsDTOS = new ArrayList<>();
        List<OpinionOfCommitteeMember> opinions = committeeOpinionRepository.findOpinionOfCommitteeMemberByWriterUsernameAndReviewed(user.getUsername(), true);

        for (OpinionOfCommitteeMember opinion : opinions) {
            commentsDTOS.add(new CommentsDTO(
                    opinion.getCommitteeMember().getUsername(),
                    opinion.getComment()
            ));
        }
        return commentsDTOS;
    }

    @Autowired
    public CommitteeOpinionServiceImpl(CommitteeOpinionRepository committeeOpinionRepository, WriterRepository writerRepository, UserService userService,
                                       LogService logService, IPAddressProvider ipAddressProvider) {
        this.committeeOpinionRepository = committeeOpinionRepository;
        this.writerRepository = writerRepository;
        this.userService = userService;
        this.logService = logService;
        this.ipAddressProvider = ipAddressProvider;
    }
}
