package nitpicksy.literarysociety2.camunda.messages;

import nitpicksy.literarysociety2.model.User;
import nitpicksy.literarysociety2.service.EmailNotificationService;
import nitpicksy.literarysociety2.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MembershipPaymentExpiredEmail implements JavaDelegate {

    private EmailNotificationService emailNotificationService;
    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        String writerUsername = (String) execution.getVariable("username");

        User writer = userService.findByUsername(writerUsername);

        String email = writer.getEmail();
        String subject = "Membership payment expired";
        String text = composeEmailToActivate();

        emailNotificationService.sendEmail(email, subject, text);
    }

    private String composeEmailToActivate() {
        StringBuilder sb = new StringBuilder();
        sb.append("Your membership 2 week payment limit has expired and your membership request has been automatically canceled.");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    @Autowired
    public MembershipPaymentExpiredEmail(EmailNotificationService emailNotificationService, UserService userService) {
        this.emailNotificationService = emailNotificationService;
        this.userService = userService;
    }
}