package nitpicksy.literarysociety2.camunda.service;

import nitpicksy.literarysociety2.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety2.model.Book;
import nitpicksy.literarysociety2.model.Log;
import nitpicksy.literarysociety2.model.OpinionOfEditor;
import nitpicksy.literarysociety2.repository.BookRepository;
import nitpicksy.literarysociety2.repository.OpinionOfEditorRepository;
import nitpicksy.literarysociety2.service.LogService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OpinionOfEditorService implements JavaDelegate {

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private BookRepository bookRepository;

    private OpinionOfEditorRepository opinionOfEditorRepository;

    private LogService logService;

    @Override
    public void execute(DelegateExecution execution) {
        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, String> map = formData.stream().collect(Collectors.toMap(FormSubmissionDTO::getFieldId, FormSubmissionDTO::getFieldValue));

        Long bookId = Long.valueOf((String) execution.getVariable("bookId"));
        Book book = bookRepository.findOneById(bookId);

        OpinionOfEditor opinionOfEditor =  new OpinionOfEditor();
        opinionOfEditor.setBook(book);
        opinionOfEditor.setEditor(book.getEditor());
        opinionOfEditor.setComment(map.get("reason"));
        opinionOfEditor.setCreated(LocalDateTime.now());
        OpinionOfEditor savedOpinionOfEditor = opinionOfEditorRepository.save(opinionOfEditor);

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ADDOE",
                String.format("Opinion of editor %s successfully created",savedOpinionOfEditor.getId())));
    }

    @Autowired
    public OpinionOfEditorService(BookRepository bookRepository, OpinionOfEditorRepository opinionOfEditorRepository,LogService logService) {
        this.bookRepository = bookRepository;
        this.opinionOfEditorRepository = opinionOfEditorRepository;
        this.logService = logService;
    }
}