package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety.enumeration.BookStatus;
import nitpicksy.literarysociety.enumeration.UserStatus;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.Genre;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.model.Writer;
import nitpicksy.literarysociety.repository.BookRepository;
import nitpicksy.literarysociety.service.GenreService;
import nitpicksy.literarysociety.service.UserService;
import nitpicksy.literarysociety.service.WriterService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CreatePublicationRequestService implements JavaDelegate {

    private CamundaService camundaService;

    private GenreService genreService;

    private WriterService writerService;

    private UserService userService;

    private BookRepository bookRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, String> map = formData.stream().collect(Collectors.toMap(FormSubmissionDTO::getFieldId, FormSubmissionDTO::getFieldValue));

        Writer writer = writerService.findByUsername((String) execution.getVariable("writer"));
        if (writer == null) {
            throw new BpmnError("greskaKreiranjeZahteva");
        }

        Long id = camundaService.extractId(map.get("selectGenre"));
        Genre genre = genreService.findById(id);

        Book bookRequest = new Book(writer, map.get("title"), map.get("synopsis"), genre, BookStatus.REQUEST_CREATED);

        List<User> editors = userService.findAllWithRoleAndStatus("ROLE_EDITOR", UserStatus.ACTIVE);
        User mainEditor = editors.get(new Random().nextInt(editors.size()));
        bookRequest.setEditor(mainEditor);
        Book savedBook = bookRepository.save(bookRequest);

        execution.setVariable("mainEditor", mainEditor.getUsername());
        execution.setVariable("bookId", savedBook.getId().toString());

        System.out.println("Main editor: " + mainEditor.getUsername());

    }

    @Autowired
    public CreatePublicationRequestService(CamundaService camundaService, GenreService genreService, WriterService writerService,
                                           UserService userService, BookRepository bookRepository) {
        this.camundaService = camundaService;
        this.genreService = genreService;
        this.writerService = writerService;
        this.userService = userService;
        this.bookRepository = bookRepository;
    }
}
