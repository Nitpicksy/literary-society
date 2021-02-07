package nitpicksy.literarysociety2.camunda.messages;

import nitpicksy.literarysociety2.model.User;
import nitpicksy.literarysociety2.service.EmailNotificationService;
import nitpicksy.literarysociety2.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditorNewPlagiarismComplaintEmail implements JavaDelegate {

    private EmailNotificationService emailNotificationService;
    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        String title = (String) execution.getVariable("title");
        String editorUsername = (String) execution.getVariable("editor");

        User editor = userService.findByUsername(editorUsername);

        String email = editor.getEmail();
        String subject = "Plagiarism review board assignee";
        String text = composeEmailToActivate(title);

        emailNotificationService.sendEmail(email, subject, text);
    }

    private String composeEmailToActivate(String title) {
        StringBuilder sb = new StringBuilder();
        sb.append("New plagiarism report with a book title '");
        sb.append(title);
        sb.append("' has been assigned to you. Please download the books, compare them and submit your review report.");
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    @Autowired
    public EditorNewPlagiarismComplaintEmail(EmailNotificationService emailNotificationService, UserService userService) {
        this.emailNotificationService = emailNotificationService;
        this.userService = userService;
    }
}
