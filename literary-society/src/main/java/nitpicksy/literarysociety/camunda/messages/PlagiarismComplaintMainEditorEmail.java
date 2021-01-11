package nitpicksy.literarysociety.camunda.messages;

import nitpicksy.literarysociety.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.service.EmailNotificationService;
import nitpicksy.literarysociety.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlagiarismComplaintMainEditorEmail implements JavaDelegate {

    private EmailNotificationService emailNotificationService;
    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, String> map = formData.stream().collect(Collectors.toMap(FormSubmissionDTO::getFieldId, FormSubmissionDTO::getFieldValue));

        String title = map.get("title");
        String mainEditorUsername = (String) execution.getVariable("mainEditor");

        User mainEditor = userService.findByUsername(mainEditorUsername);

        String email = mainEditor.getEmail();
        String subject = "New plagiarism report";
        String text = composeEmailToActivate(title);

        emailNotificationService.sendEmail(email, subject, text);
    }

    private String composeEmailToActivate(String title) {
        StringBuilder sb = new StringBuilder();
        sb.append("New plagiarism report with a book title '");
        sb.append(title);
        sb.append("' has been assigned to you. Please assign editors to review the report.");
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    @Autowired
    public PlagiarismComplaintMainEditorEmail(EmailNotificationService emailNotificationService, UserService userService) {
        this.emailNotificationService = emailNotificationService;
        this.userService = userService;
    }
}
