package nitpicksy.paymentgateway.serviceimpl;

import nitpicksy.paymentgateway.service.EmailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private JavaMailSender javaMailSender;

    private Environment env;

    @Override
    @Async
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(env.getProperty("spring.mail.username"));
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(text);

        javaMailSender.send(mail);
        System.out.println(String.format("Email '%s' sent to %s at %s", subject, to, LocalDateTime.now().toLocalTime()));
    }

    @Autowired
    public EmailNotificationServiceImpl(JavaMailSender javaMailSender, Environment env) {
        this.javaMailSender = javaMailSender;
        this.env = env;
    }
}
