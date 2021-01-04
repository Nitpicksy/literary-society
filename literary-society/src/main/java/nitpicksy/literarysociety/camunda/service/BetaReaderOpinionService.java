package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.OpinionOfBetaReader;
import nitpicksy.literarysociety.model.Reader;
import nitpicksy.literarysociety.repository.OpinionOfBetaReaderRepository;
import nitpicksy.literarysociety.service.BookService;
import nitpicksy.literarysociety.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BetaReaderOpinionService implements JavaDelegate {

    private BookService bookService;

    private UserService userService;

    private OpinionOfBetaReaderRepository opinionOfBetaReaderRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, String> map = formData.stream().collect(Collectors.toMap(FormSubmissionDTO::getFieldId, FormSubmissionDTO::getFieldValue));
        String comment = map.get("comment");

        Long bookId = Long.valueOf((String) execution.getVariable("bookId"));
        Book book = bookService.findById(bookId);

        String betaReaderUsername = (String) execution.getVariable("betaReader");
        Reader betaReader = (Reader) userService.findByUsername(betaReaderUsername);

        OpinionOfBetaReader opinionOfBetaReader = new OpinionOfBetaReader(null, betaReader, book, comment);
        opinionOfBetaReaderRepository.save(opinionOfBetaReader);
    }

    @Autowired
    public BetaReaderOpinionService(BookService bookService, UserService userService, OpinionOfBetaReaderRepository opinionOfBetaReaderRepository) {
        this.bookService = bookService;
        this.userService = userService;
        this.opinionOfBetaReaderRepository = opinionOfBetaReaderRepository;
    }
}
