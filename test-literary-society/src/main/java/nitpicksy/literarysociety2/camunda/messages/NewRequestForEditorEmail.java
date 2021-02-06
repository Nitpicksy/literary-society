package nitpicksy.literarysociety2.camunda.messages;

import nitpicksy.literarysociety2.model.User;
import nitpicksy.literarysociety2.service.EmailNotificationService;
import nitpicksy.literarysociety2.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewRequestForEditorEmail implements JavaDelegate {

    private EmailNotificationService emailNotificationService;

    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) {

        String title = (String) execution.getVariable("title");
        String mainEditorUsername = (String) execution.getVariable("mainEditor");

        User mainEditor = userService.findByUsername(mainEditorUsername);

        String email = mainEditor.getEmail();
        String subject = "New publication request";
        String text = composeEmailToActivate(title);

        emailNotificationService.sendEmail(email, subject, text);
    }

    private String composeEmailToActivate(String title) {
        StringBuilder sb = new StringBuilder();
        sb.append("New publication request under title '");
        sb.append(title);
        sb.append("' has been assigned to you. You can accept it for a review or reject it immediately in your tasks panel.");
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    @Autowired
    public NewRequestForEditorEmail(EmailNotificationService emailNotificationService, UserService userService) {
        this.emailNotificationService = emailNotificationService;
        this.userService = userService;
    }
}
