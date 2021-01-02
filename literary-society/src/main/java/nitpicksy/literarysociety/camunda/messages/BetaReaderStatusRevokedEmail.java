package nitpicksy.literarysociety.camunda.messages;

import nitpicksy.literarysociety.model.Reader;
import nitpicksy.literarysociety.service.EmailNotificationService;
import nitpicksy.literarysociety.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BetaReaderStatusRevokedEmail implements JavaDelegate {

    private EmailNotificationService emailNotificationService;

    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) {
        String betaReaderUsername = (String) execution.getVariable("betaReader");
        Reader betaReader = (Reader) userService.findByUsername(betaReaderUsername);

        String email = betaReader.getEmail();
        String subject = "Beta-reader status revoked";
        String text = composeEmailToActivate();

        emailNotificationService.sendEmail(email, subject, text);
    }

    private String composeEmailToActivate() {
        StringBuilder sb = new StringBuilder();
        sb.append("Your penalty score reached maximum limit which is 5. Therefore, your status of a beta-reader is revoked. ");
        sb.append("You can still use Literary Society as a regular reader.");
        return sb.toString();
    }

    @Autowired
    public BetaReaderStatusRevokedEmail(EmailNotificationService emailNotificationService, UserService userService) {
        this.emailNotificationService = emailNotificationService;
        this.userService = userService;
    }
}
