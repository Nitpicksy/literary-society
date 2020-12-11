package nitpicksy.literarysociety.camunda.notifications;

import nitpicksy.literarysociety.service.EmailNotificationService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendEmail implements JavaDelegate {

    private final EmailNotificationService emailNotificationService;

    @Override
    public void execute(DelegateExecution execution) {

        String subject = (String) execution.getVariable("subject");
        String email = (String) execution.getVariable("email");
        String text = (String) execution.getVariable("text");

        System.out.println(subject + " " + email + " " + text);

        emailNotificationService.sendEmail(email, subject, text);

    }

    @Autowired
    public SendEmail(EmailNotificationService emailNotificationService) {
        this.emailNotificationService = emailNotificationService;
    }

}
