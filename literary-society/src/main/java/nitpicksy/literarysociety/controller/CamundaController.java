package nitpicksy.literarysociety.controller;

import nitpicksy.literarysociety.dto.request.FormSubmissionDTO;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/process", produces = MediaType.APPLICATION_JSON_VALUE)
public class CamundaController {

    private TaskService taskService;

    private RuntimeService runtimeService;

    private FormService formService;

    @PostMapping(path = "/{taskId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> submitForm(@Valid @RequestBody List<FormSubmissionDTO> formDTOList, @PathVariable String taskId) {
        // Remove all injected select fields to bypass Camunda validation
        List<FormSubmissionDTO> filteredList = formDTOList.stream()
                .filter(formField -> !(formField.getFieldId().startsWith("select")))
                .collect(Collectors.toList());

        Map<String, Object> fieldsMap = filteredList.stream()
                .collect(Collectors.toMap(FormSubmissionDTO::getFieldId, FormSubmissionDTO::getFieldValue));

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        runtimeService.setVariable(processInstanceId, "formData", formDTOList);
        formService.submitTaskForm(taskId, fieldsMap);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Autowired
    public CamundaController(TaskService taskService, RuntimeService runtimeService, FormService formService) {
        this.taskService = taskService;
        this.runtimeService = runtimeService;
        this.formService = formService;
    }
}
