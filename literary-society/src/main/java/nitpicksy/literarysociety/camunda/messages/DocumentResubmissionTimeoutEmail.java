package nitpicksy.literarysociety.camunda.messages;

import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.service.EmailNotificationService;
import nitpicksy.literarysociety.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentResubmissionTimeoutEmail implements JavaDelegate {

    private EmailNotificationService emailNotificationService;
    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        String writerUsername = (String) execution.getVariable("writer");

        User writer = userService.findByUsername(writerUsername);

        String email = writer.getEmail();
        String subject = "Membership - document resubmission timeout";
        String text = composeEmailToActivate();

        emailNotificationService.sendEmail(email, subject, text);
    }

    private String composeEmailToActivate() {
        StringBuilder sb = new StringBuilder();
        sb.append("Your membership request document resubmission has been timed out. Your membership request has been automatically declined.");
        return sb.toString();
    }

    @Autowired
    public DocumentResubmissionTimeoutEmail(EmailNotificationService emailNotificationService, UserService userService) {
        this.emailNotificationService = emailNotificationService;
        this.userService = userService;
    }
}