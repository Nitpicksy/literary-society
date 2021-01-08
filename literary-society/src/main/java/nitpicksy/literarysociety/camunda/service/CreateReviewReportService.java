package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety.model.OpinionOfEditorAboutComplaint;
import nitpicksy.literarysociety.model.PlagiarismComplaint;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.repository.BookRepository;
import nitpicksy.literarysociety.repository.OpinionOfEditorAboutComplaintRepository;
import nitpicksy.literarysociety.repository.PlagiarismComplaintRepository;
import nitpicksy.literarysociety.service.UserService;
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

    private UserService userService;

    private BookRepository bookRepository;

    private PlagiarismComplaintRepository plagiarismComplaintRepository;

    private OpinionOfEditorAboutComplaintRepository opinionOfEditorAboutComplaintRepository;

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
        opinionOfEditorAboutComplaintRepository.save(opinion);
    }

    @Autowired
    public CreateReviewReportService(UserService userService, BookRepository bookRepository, PlagiarismComplaintRepository plagiarismComplaintRepository, OpinionOfEditorAboutComplaintRepository opinionOfEditorAboutComplaintRepository) {
        this.userService = userService;
        this.bookRepository = bookRepository;
        this.plagiarismComplaintRepository = plagiarismComplaintRepository;
        this.opinionOfEditorAboutComplaintRepository = opinionOfEditorAboutComplaintRepository;
    }
}
