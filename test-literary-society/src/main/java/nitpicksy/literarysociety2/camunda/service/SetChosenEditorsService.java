package nitpicksy.literarysociety2.camunda.service;

import nitpicksy.literarysociety2.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety2.model.PlagiarismComplaint;
import nitpicksy.literarysociety2.model.User;
import nitpicksy.literarysociety2.repository.PlagiarismComplaintRepository;
import nitpicksy.literarysociety2.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SetChosenEditorsService implements JavaDelegate {

    private CamundaService camundaService;

    private UserService userService;

    private PlagiarismComplaintRepository plagiarismComplaintRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, String> map = formData.stream().collect(Collectors.toMap(FormSubmissionDTO::getFieldId, FormSubmissionDTO::getFieldValue));
        List<Long> editorIds = camundaService.extractIds(map.get("selectEditors"));

        Long id = Long.valueOf((String) execution.getVariable("plagiarismId"));

        List<User> editors = userService.findByIds(editorIds);
        List<String> editorsUsernames = editors.stream().map(User::getUsername).collect(Collectors.toList());

        execution.setVariable("chosenEditors", editorsUsernames);
        System.out.println("Editors: " + editorsUsernames);

        PlagiarismComplaint plagiarismComplaint = plagiarismComplaintRepository.findById(id).orElse(null);
        plagiarismComplaint.setEditors(new HashSet<>(editors));
        plagiarismComplaintRepository.save(plagiarismComplaint);
    }

    @Autowired
    public SetChosenEditorsService(CamundaService camundaService, UserService userService, PlagiarismComplaintRepository plagiarismComplaintRepository) {
        this.camundaService = camundaService;
        this.userService = userService;
        this.plagiarismComplaintRepository = plagiarismComplaintRepository;
    }
}
