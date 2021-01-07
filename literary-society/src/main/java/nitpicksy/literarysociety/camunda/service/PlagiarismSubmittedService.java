package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety.enumeration.UserStatus;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.repository.BookRepository;
import nitpicksy.literarysociety.service.UserService;
import nitpicksy.literarysociety.service.WriterService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class PlagiarismSubmittedService implements JavaDelegate {

    private CamundaService camundaService;

    private WriterService writerService;

    private UserService userService;

    private BookRepository bookRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, String> map = formData.stream().collect(Collectors.toMap(FormSubmissionDTO::getFieldId, FormSubmissionDTO::getFieldValue));

        Long id = Long.valueOf(map.get("book"));


        List<User> editors = userService.findAllWithRoleAndStatus("ROLE_EDITOR", UserStatus.ACTIVE);
        User mainEditor = editors.get(new Random().nextInt(editors.size()));

        execution.setVariable("mainEditor", mainEditor.getUsername());
//        execution.setVariable("bookId", savedBook.getId().toString());

        System.out.println("Main editor: " + mainEditor.getUsername());
    }

    @Autowired
    public PlagiarismSubmittedService(CamundaService camundaService, WriterService writerService, UserService userService, BookRepository bookRepository) {
        this.camundaService = camundaService;
        this.writerService = writerService;
        this.userService = userService;
        this.bookRepository = bookRepository;
    }
}