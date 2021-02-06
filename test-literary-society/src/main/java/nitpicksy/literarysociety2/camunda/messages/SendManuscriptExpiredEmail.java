package nitpicksy.literarysociety2.camunda.messages;

import nitpicksy.literarysociety2.model.Book;
import nitpicksy.literarysociety2.model.User;
import nitpicksy.literarysociety2.service.BookService;
import nitpicksy.literarysociety2.service.EmailNotificationService;
import nitpicksy.literarysociety2.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendManuscriptExpiredEmail implements JavaDelegate {

    private EmailNotificationService emailNotificationService;

    private UserService userService;

    private BookService bookService;

    @Override
    public void execute(DelegateExecution execution) {
        Long bookId = Long.valueOf((String) execution.getVariable("bookId"));
        Book book = bookService.findById(bookId);

        String writerUsername = (String) execution.getVariable("writer");

        User writer = userService.findByUsername(writerUsername);

        String email = writer.getEmail();
        String subject = "Deadline to send manuscript expired";
        String text = composeEmailToActivate(book.getTitle());

        emailNotificationService.sendEmail(email, subject, text);
    }

    private String composeEmailToActivate(String title) {
        StringBuilder sb = new StringBuilder();
        sb.append("Manuscript for publication request '");
        sb.append(title);
        sb.append("' not sent in time. Therefore, publication request is closed.");
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    @Autowired
    public SendManuscriptExpiredEmail(EmailNotificationService emailNotificationService, UserService userService, BookService bookService) {
        this.emailNotificationService = emailNotificationService;
        this.userService = userService;
        this.bookService = bookService;
    }
}