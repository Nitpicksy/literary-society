package nitpicksy.literarysociety.camunda.messages;

import nitpicksy.literarysociety.dto.camunda.PublicationRequestDTO;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.service.BookService;
import nitpicksy.literarysociety.service.EmailNotificationService;
import nitpicksy.literarysociety.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestIsRejectedEmail implements JavaDelegate {

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
        String subject = "Publication request is rejected";
        String text = composeEmailToActivate(publicationRequestDTO.getTitle(),(String) execution.getVariable("reason"));

        emailNotificationService.sendEmail(email, subject, text);
    }

    private String composeEmailToActivate(String title,String reason) {
        StringBuilder sb = new StringBuilder();
        sb.append("Publication request for book '");
        sb.append(title);
        sb.append("' is rejected. Reason for rejection is: ");
        sb.append(reason);
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    @Autowired
    public RequestIsRejectedEmail(EmailNotificationService emailNotificationService, UserService userService, BookService bookService) {
        this.emailNotificationService = emailNotificationService;
        this.userService = userService;
        this.bookService = bookService;
    }
}
