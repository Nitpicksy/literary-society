package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety.enumeration.BookStatus;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.OpinionOfEditor;
import nitpicksy.literarysociety.repository.BookRepository;
import nitpicksy.literarysociety.repository.OpinionOfEditorRepository;
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

    private BookRepository bookRepository;

    private OpinionOfEditorRepository opinionOfEditorRepository;

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
        opinionOfEditorRepository.save(opinionOfEditor);

    }

    @Autowired
    public OpinionOfEditorService(BookRepository bookRepository, OpinionOfEditorRepository opinionOfEditorRepository) {
        this.bookRepository = bookRepository;
        this.opinionOfEditorRepository = opinionOfEditorRepository;
    }
}