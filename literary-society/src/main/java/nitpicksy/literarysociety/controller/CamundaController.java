package nitpicksy.literarysociety.controller;

import nitpicksy.literarysociety.dto.request.FormSubmissionDTO;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.camunda.bpm.engine.TaskService;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/api/process", produces = MediaType.APPLICATION_JSON_VALUE)
public class CamundaController {

    private TaskService taskService;

    private RuntimeService runtimeService;

    private FormService formService;

    @PostMapping(path = "/{taskId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> submitForm(@Valid  @RequestBody List<FormSubmissionDTO> formDTO, @PathVariable String taskId) {
        HashMap<String, Object> map = this.mapListToDto(formDTO);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        runtimeService.setVariable(processInstanceId, "formData", formDTO);
        formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private HashMap<String, Object> mapListToDto(List<FormSubmissionDTO> list)
    {
        HashMap<String, Object> map = new HashMap<>();
        for(FormSubmissionDTO temp : list){
            map.put(temp.getFieldId(), temp.getFieldValue());
        }
        return map;
    }

    @Autowired
    public CamundaController(TaskService taskService, RuntimeService runtimeService, FormService formService) {
        this.taskService = taskService;
        this.runtimeService = runtimeService;
        this.formService = formService;
    }
}
