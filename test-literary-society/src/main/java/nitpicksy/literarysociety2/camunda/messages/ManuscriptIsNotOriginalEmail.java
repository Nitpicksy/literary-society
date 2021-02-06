package nitpicksy.literarysociety2.camunda.messages;

import nitpicksy.literarysociety2.dto.camunda.PublicationRequestDTO;
import nitpicksy.literarysociety2.model.User;
import nitpicksy.literarysociety2.service.BookService;
import nitpicksy.literarysociety2.service.EmailNotificationService;
import nitpicksy.literarysociety2.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManuscriptIsNotOriginalEmail implements JavaDelegate {

    private EmailNotificationService emailNotificationService;

    private UserService userService;

    private BookService bookService;

    @Override
    public void execute(DelegateExecution execution) {
        Long bookId = Long.valueOf((String) execution.getVariable("bookId"));
        PublicationRequestDTO publicationRequestDTO = bookService.getPublicationRequest(bookId);

        String writerUsername = (String) execution.getVariable("writer");

        User writer = userService.findByUsername(writerUsername);

        String email = writer.getEmail();
        String subject = "Manuscript is nor original";
        String text = composeEmailToActivate(publicationRequestDTO.getTitle(), (String) execution.getVariable("reason"));

        emailNotificationService.sendEmail(email, subject, text);
    }

    private String composeEmailToActivate(String title, String reason) {
        StringBuilder sb = new StringBuilder();
        sb.append("Manuscript '");
        sb.append(title);
        sb.append("' is nor original. Editor thinks: ");
        sb.append(reason);
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    @Autowired
    public ManuscriptIsNotOriginalEmail(EmailNotificationService emailNotificationService, UserService userService, BookService bookService) {
        this.emailNotificationService = emailNotificationService;
        this.userService = userService;
        this.bookService = bookService;
    }
}