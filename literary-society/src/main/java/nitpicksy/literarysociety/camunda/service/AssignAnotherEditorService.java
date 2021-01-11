package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety.model.PlagiarismComplaint;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.repository.PlagiarismComplaintRepository;
import nitpicksy.literarysociety.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AssignAnotherEditorService implements JavaDelegate {

    private CamundaService camundaService;

    private UserService userService;

    private PlagiarismComplaintRepository plagiarismComplaintRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, String> map = formData.stream().collect(Collectors.toMap(FormSubmissionDTO::getFieldId, FormSubmissionDTO::getFieldValue));
        List<Long> editorIds = camundaService.extractIds(map.get("selectOtherEditors"));

        Long id = Long.valueOf((String) execution.getVariable("plagiarismId"));

        List<User> editors = userService.findByIds(editorIds);
        List<String> editorsUsernames = editors.stream().map(User::getUsername).collect(Collectors.toList());

        String lazyEditor = (String) execution.getVariable("editor");
        execution.setVariable("editor", editorsUsernames.get(0));
        System.out.println("Lazy editor " + lazyEditor + " replaced with editor: " + editorsUsernames);

        PlagiarismComplaint plagiarismComplaint = plagiarismComplaintRepository.findById(id).orElse(null);
        HashSet<User> newEditors = new HashSet<>();
        for (User e : plagiarismComplaint.getEditors()) {
            if (!e.getUsername().equals(lazyEditor)) {
                newEditors.add(e);
            }
        }

        newEditors.add(editors.get(0));
        plagiarismComplaint.setEditors(new HashSet<>(newEditors));
        plagiarismComplaintRepository.save(plagiarismComplaint);

    }

    @Autowired
    public AssignAnotherEditorService(CamundaService camundaService, UserService userService, PlagiarismComplaintRepository plagiarismComplaintRepository) {
        this.camundaService = camundaService;
        this.userService = userService;
        this.plagiarismComplaintRepository = plagiarismComplaintRepository;
    }
}
