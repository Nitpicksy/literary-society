package nitpicksy.literarysociety.camunda.messages;

import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.service.EmailNotificationService;
import nitpicksy.literarysociety.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenExpiredEmail implements JavaDelegate {

    private EmailNotificationService emailNotificationService;

    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) {

        String username = (String) execution.getVariable("username");

        User user = userService.findByUsername(username);

        String email = user.getEmail();
        String subject = "Activation link expired";
        String text = composeEmailToActivate();

        emailNotificationService.sendEmail(email, subject, text);
    }

    private String composeEmailToActivate() {
        StringBuilder sb = new StringBuilder();
        sb.append("Link for activating your account has expired. The account will be suspended.");
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    @Autowired
    public TokenExpiredEmail(EmailNotificationService emailNotificationService, UserService userService) {
        this.emailNotificationService = emailNotificationService;
        this.userService = userService;
    }

}
