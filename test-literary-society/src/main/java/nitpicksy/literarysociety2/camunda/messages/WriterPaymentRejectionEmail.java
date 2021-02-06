package nitpicksy.literarysociety2.camunda.messages;

import nitpicksy.literarysociety2.model.Writer;
import nitpicksy.literarysociety2.service.EmailNotificationService;
import nitpicksy.literarysociety2.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WriterPaymentRejectionEmail implements JavaDelegate {

    private EmailNotificationService emailNotificationService;
    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String username = (String) execution.getVariable("user");
        Writer writer = (Writer) userService.findByUsername(username);
        String email = writer.getEmail();
        String subject = "Payment request rejected";
        String text = composeEmailToActivate();

        emailNotificationService.sendEmail(email, subject, text);
    }

    private String composeEmailToActivate() {
        StringBuilder sb = new StringBuilder();
        sb.append("Your payment request has been processed and is rejected. A new payment task is assigned to your account. ");
        sb.append("Please keep in mind that payment time limit is 14 days.");
        return sb.toString();
    }

    @Autowired
    public WriterPaymentRejectionEmail(EmailNotificationService emailNotificationService, UserService userService) {
        this.emailNotificationService = emailNotificationService;
        this.userService = userService;
    }
}
