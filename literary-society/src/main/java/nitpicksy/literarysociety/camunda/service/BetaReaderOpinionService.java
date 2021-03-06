package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.Log;
import nitpicksy.literarysociety.model.OpinionOfBetaReader;
import nitpicksy.literarysociety.model.Reader;
import nitpicksy.literarysociety.repository.OpinionOfBetaReaderRepository;
import nitpicksy.literarysociety.service.BookService;
import nitpicksy.literarysociety.service.LogService;
import nitpicksy.literarysociety.service.UserService;
import nitpicksy.literarysociety.utils.IPAddressProvider;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BetaReaderOpinionService implements JavaDelegate {

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private BookService bookService;

    private UserService userService;

    private OpinionOfBetaReaderRepository opinionOfBetaReaderRepository;

    private LogService logService;

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
        OpinionOfBetaReader savedOpinionOfBetaReader = opinionOfBetaReaderRepository.save(opinionOfBetaReader);

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ADDOBR",
                String.format("Opinion of Beta Reader %s successfully created",savedOpinionOfBetaReader.getId())));
    }

    @Autowired
    public BetaReaderOpinionService(BookService bookService, UserService userService,
                                    OpinionOfBetaReaderRepository opinionOfBetaReaderRepository,LogService logService) {
        this.bookService = bookService;
        this.userService = userService;
        this.opinionOfBetaReaderRepository = opinionOfBetaReaderRepository;
        this.logService = logService;
    }
}
