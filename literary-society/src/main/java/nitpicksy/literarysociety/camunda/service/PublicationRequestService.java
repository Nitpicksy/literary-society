package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety.enumeration.BookStatus;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.repository.BookRepository;
import nitpicksy.literarysociety.service.GenreService;
import nitpicksy.literarysociety.service.UserService;
import nitpicksy.literarysociety.service.WriterService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PublicationRequestService implements JavaDelegate {

    private CamundaService camundaService;

    private GenreService genreService;

    private WriterService writerService;

    private UserService userService;

    private BookRepository bookRepository;

    @Override
    public void execute(DelegateExecution execution)  {
        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, String> map = formData.stream().collect(Collectors.toMap(FormSubmissionDTO::getFieldId, FormSubmissionDTO::getFieldValue));
        Long bookId = Long.valueOf((String) execution.getVariable("bookId"));

        Book book = bookRepository.findOneById(bookId);
        book.setStatus(BookStatus.valueOf(map.get("decision")));
        BookStatus bookStatus = book.getStatus();
        bookRepository.save(book);

        if(bookStatus.equals(BookStatus.TO_BE_SENT)){
            execution.setVariable("rejected",false);
        }
        if(bookStatus.equals(BookStatus.REQUEST_REJECTED)){
            execution.setVariable("rejected",true);
        }

        if(bookStatus.equals(BookStatus.ORIGINAL)){
            execution.setVariable("original",true);
        }
        if(bookStatus.equals(BookStatus.NOT_ORIGINAL)){
            execution.setVariable("original",false);
        }

        if(bookStatus.equals(BookStatus.ACCEPTED)){
            execution.setVariable("rejected",false);
        }
        if(bookStatus.equals(BookStatus.REQUEST_REJECTED)){
            execution.setVariable("rejected",true);
        }
    }

    @Autowired
    public PublicationRequestService(CamundaService camundaService, GenreService genreService, WriterService writerService,
                                           UserService userService, BookRepository bookRepository) {
        this.camundaService = camundaService;
        this.genreService = genreService;
        this.writerService = writerService;
        this.userService = userService;
        this.bookRepository = bookRepository;
    }
}

