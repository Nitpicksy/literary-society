package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.dto.camunda.EnumKeyValueDTO;
import nitpicksy.literarysociety.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety.enumeration.UserStatus;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.PlagiarismComplaint;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.repository.BookRepository;
import nitpicksy.literarysociety.repository.PlagiarismComplaintRepository;
import nitpicksy.literarysociety.service.UserService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class PlagiarismSubmittedService implements JavaDelegate {

    private UserService userService;

    private BookRepository bookRepository;

    private PlagiarismComplaintRepository plagiarismComplaintRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, String> map = formData.stream().collect(Collectors.toMap(FormSubmissionDTO::getFieldId, FormSubmissionDTO::getFieldValue));


        List<User> editors = userService.findAllWithRoleAndStatus("ROLE_EDITOR", UserStatus.ACTIVE);
        User mainEditor = editors.get(new Random().nextInt(editors.size()));

        execution.setVariable("mainEditor", mainEditor.getUsername());
        System.out.println("Main editor: " + mainEditor.getUsername());

        List<EnumKeyValueDTO> enumList = new ArrayList<>();
        for (User editor : editors) {
            if (editor.getUsername().equals(mainEditor.getUsername())) {
                continue;
            }
            String key = "id_" + editor.getUserId();
            String editorInfo = editor.getFirstName() + " " + editor.getLastName();
            enumList.add(new EnumKeyValueDTO(key, editorInfo));
        }
        execution.setVariable("selectEditorsList", enumList);

        //save the plagiarism report
        savePlagiarismReport(execution, map);
    }

    private void savePlagiarismReport(DelegateExecution execution, Map<String, String> map) {
        Long id = Long.valueOf(map.get("book"));
        String title = map.get("title");
        String writersName = map.get("writer");

        Book writersBook = bookRepository.findOneById(id);
        Book reportedBook = bookRepository.findFirstByTitleContainingAndWritersNamesContaining(title, writersName);
        if (reportedBook == null) {
            throw new BpmnError("greskaKnjigaNePostoji");
        }

        execution.setVariable("title", reportedBook.getTitle());
        execution.setVariable("writersName", reportedBook.getWritersNames());
        execution.setVariable("writer", writersBook.getWriter().getUsername());

        PlagiarismComplaint plagiarismComplaint = this.plagiarismComplaintRepository.save(new PlagiarismComplaint(writersBook.getWriter(), writersBook, reportedBook));
        execution.setVariable("plagiarismId", plagiarismComplaint.getId().toString());
    }

    @Autowired
    public PlagiarismSubmittedService(UserService userService, BookRepository bookRepository, PlagiarismComplaintRepository plagiarismComplaintRepository) {
        this.userService = userService;
        this.bookRepository = bookRepository;
        this.plagiarismComplaintRepository = plagiarismComplaintRepository;
    }
}