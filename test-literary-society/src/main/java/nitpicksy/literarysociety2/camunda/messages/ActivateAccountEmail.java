package nitpicksy.literarysociety2.camunda.messages;

import nitpicksy.literarysociety2.model.User;
import nitpicksy.literarysociety2.service.EmailNotificationService;
import nitpicksy.literarysociety2.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class ActivateAccountEmail implements JavaDelegate {

    private EmailNotificationService emailNotificationService;

    private UserService userService;

    private Environment environment;

    @Override
    public void execute(DelegateExecution execution) {

        String username = (String) execution.getVariable("username");
        String token = (String) execution.getVariable("token");

        User user = userService.findByUsername(username);

        String email = user.getEmail();
        String subject = "Activate account";
        String text = composeEmailToActivate(token, execution.getProcessInstanceId());

        System.out.println(System.lineSeparator() + text);

        emailNotificationService.sendEmail(email, subject, text);
    }

    private String composeEmailToActivate(String token, String processInstanceId) {
        StringBuilder sb = new StringBuilder();
        sb.append("You have successfully registered to the Literary Society website.");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("To activate your account click the following link:");
        sb.append(System.lineSeparator());
        sb.append(getLocalhostURL());
        sb.append(String.format("activate-account?piId=%s&t=%s", processInstanceId, token));

        return sb.toString();
    }

    private String getLocalhostURL() {
        return environment.getProperty("LOCALHOST_URL");
    }

    @Autowired
    public ActivateAccountEmail(EmailNotificationService emailNotificationService, UserService userService, Environment environment) {
        this.emailNotificationService = emailNotificationService;
        this.userService = userService;
        this.environment = environment;
    }
}
