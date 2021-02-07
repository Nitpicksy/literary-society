package nitpicksy.literarysociety2.camunda.service;

import nitpicksy.literarysociety2.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety2.model.Log;
import nitpicksy.literarysociety2.model.OpinionOfEditorAboutComplaint;
import nitpicksy.literarysociety2.model.PlagiarismComplaint;
import nitpicksy.literarysociety2.model.User;
import nitpicksy.literarysociety2.repository.BookRepository;
import nitpicksy.literarysociety2.repository.OpinionOfEditorAboutComplaintRepository;
import nitpicksy.literarysociety2.repository.PlagiarismComplaintRepository;
import nitpicksy.literarysociety2.service.LogService;
import nitpicksy.literarysociety2.service.UserService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CreateReviewReportService implements JavaDelegate {

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private UserService userService;

    private BookRepository bookRepository;

    private PlagiarismComplaintRepository plagiarismComplaintRepository;

    private OpinionOfEditorAboutComplaintRepository opinionOfEditorAboutComplaintRepository;

    private LogService logService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, String> map = formData.stream().collect(Collectors.toMap(FormSubmissionDTO::getFieldId, FormSubmissionDTO::getFieldValue));
        String review = map.get("review");

        Long plagiarismId = Long.valueOf((String) execution.getVariable("plagiarismId"));
        PlagiarismComplaint plagiarismComplaint = plagiarismComplaintRepository.findById(plagiarismId).orElse(null);
        if (plagiarismComplaint == null) {
            throw new BpmnError("greskaKreiranjeBeleske");
        }

        String editorUsername = (String) execution.getVariable("editor");
        User editor = userService.findByUsername(editorUsername);

        OpinionOfEditorAboutComplaint opinion = new OpinionOfEditorAboutComplaint(editor, review, plagiarismComplaint);
        OpinionOfEditorAboutComplaint savedOpinion = opinionOfEditorAboutComplaintRepository.save(opinion);

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ADDOEC",
                String.format("Opinion of Editor %s successfully created",savedOpinion.getId())));

    }

    @Autowired
    public CreateReviewReportService(UserService userService, BookRepository bookRepository,
                                     PlagiarismComplaintRepository plagiarismComplaintRepository,
                                     OpinionOfEditorAboutComplaintRepository opinionOfEditorAboutComplaintRepository,LogService logService) {
        this.userService = userService;
        this.bookRepository = bookRepository;
        this.plagiarismComplaintRepository = plagiarismComplaintRepository;
        this.opinionOfEditorAboutComplaintRepository = opinionOfEditorAboutComplaintRepository;
        this.logService = logService;
    }
}
