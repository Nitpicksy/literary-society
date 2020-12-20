package nitpicksy.literarysociety.controller;

import nitpicksy.literarysociety.camunda.service.CamundaService;
import nitpicksy.literarysociety.constants.CamundaConstants;
import nitpicksy.literarysociety.dto.response.FormFieldsDTO;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/readers", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReaderController {

    private CamundaService camundaService;

    private TaskService taskService;

    @GetMapping("/start-registration")
    public ResponseEntity<FormFieldsDTO> getRegistrationFields() {
        FormFieldsDTO formFieldsDTO = camundaService.start(CamundaConstants.PROCESS_READER_REGISTRATION);
        return new ResponseEntity<>(camundaService.setEnumValues(formFieldsDTO), HttpStatus.OK);
    }

    @GetMapping("/beta/choose-genres")
    public ResponseEntity<FormFieldsDTO> betaReaderChooseGenres(@RequestParam String piId) {
        Task task = taskService.createTaskQuery().processInstanceId(piId).list().get(0);
        FormFieldsDTO formFieldsDTO = camundaService.getFormFields(task.getProcessInstanceId(), task.getId());
        return new ResponseEntity<>(camundaService.setEnumValues(formFieldsDTO), HttpStatus.OK);
    }

    @Autowired
    public ReaderController(CamundaService camundaService, TaskService taskService) {
        this.camundaService = camundaService;
        this.taskService = taskService;
    }
}
