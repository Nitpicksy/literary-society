package nitpicksy.literarysociety.camunda.messages;

import nitpicksy.literarysociety.model.Writer;
import nitpicksy.literarysociety.service.EmailNotificationService;
import nitpicksy.literarysociety.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlagiarismResultEmail implements JavaDelegate {


    private EmailNotificationService emailNotificationService;
    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        String writerUsername = (String) execution.getVariable("writer");

        Writer writer = (Writer) userService.findByUsername(writerUsername);

        boolean decision = (boolean) execution.getVariable("plagiarism");

        String email = writer.getEmail();
        String subject = "Plagiarism report evaluation";
        String text = composeEmailToActivate(decision);

        emailNotificationService.sendEmail(email, subject, text);
    }

    private String composeEmailToActivate(boolean decision) {
        StringBuilder sb = new StringBuilder();
        sb.append("Your plagiarism complaint has been proccessed and the book is considered: ");
        if (decision) {
            sb.append("PLAGIARISM");
        } else {
            sb.append("NOT_PLAGIARISM");
        }
        sb.append("Thank you for your complaint.");
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    @Autowired
    public PlagiarismResultEmail(EmailNotificationService emailNotificationService, UserService userService) {
        this.emailNotificationService = emailNotificationService;
        this.userService = userService;
    }
}
