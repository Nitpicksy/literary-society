package nitpicksy.literarysociety2.camunda.messages;

import nitpicksy.literarysociety2.model.Writer;
import nitpicksy.literarysociety2.service.EmailNotificationService;
import nitpicksy.literarysociety2.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WriterPaymentSuccessEmail implements JavaDelegate {

    private EmailNotificationService emailNotificationService;
    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String username = (String) execution.getVariable("user");
        Writer writer = (Writer) userService.findByUsername(username);
        String email = writer.getEmail();
        String subject = "Payment successful";
        String text = composeEmailToActivate();

        emailNotificationService.sendEmail(email, subject, text);
    }

    private String composeEmailToActivate() {
        StringBuilder sb = new StringBuilder();
        sb.append("Your payment request has been processed and is approved. ");
        sb.append("Welcome to the our literary society");
        return sb.toString();
    }

    @Autowired
    public WriterPaymentSuccessEmail(EmailNotificationService emailNotificationService, UserService userService) {
        this.emailNotificationService = emailNotificationService;
        this.userService = userService;
    }
}