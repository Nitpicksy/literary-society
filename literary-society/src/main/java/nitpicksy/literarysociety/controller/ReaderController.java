package nitpicksy.literarysociety.controller;

import nitpicksy.literarysociety.camunda.service.CamundaService;
import nitpicksy.literarysociety.constants.CamundaConstants;
import nitpicksy.literarysociety.dto.response.FormFieldsDTO;
import nitpicksy.literarysociety.dto.response.ProcessDataDTO;
import nitpicksy.literarysociety.model.Log;
import nitpicksy.literarysociety.service.LogService;
import nitpicksy.literarysociety.utils.IPAddressProvider;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping(value = "/api/readers", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReaderController {

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private CamundaService camundaService;

    private TaskService taskService;

    private LogService logService;

    private IPAddressProvider ipAddressProvider;

    @GetMapping("/start-registration")
    public ResponseEntity<ProcessDataDTO> startProcess() {
        ProcessDataDTO processDataDTO = camundaService.start(CamundaConstants.PROCESS_READER_REGISTRATION);
        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "RREG",
                String.format("User from IP address %s started reader's registration process.", ipAddressProvider.get())));
        return new ResponseEntity<>(processDataDTO, HttpStatus.OK);
    }

    @GetMapping("/registration-form")
    public ResponseEntity<FormFieldsDTO> getRegistrationForm(@NotBlank @RequestParam String piId, @NotBlank @RequestParam String taskId) {
        FormFieldsDTO formFieldsDTO = camundaService.getFormFields(piId, taskId);
        return new ResponseEntity<>(camundaService.setEnumValues(formFieldsDTO), HttpStatus.OK);
    }

    @GetMapping("/beta/choose-genres")
    public ResponseEntity<FormFieldsDTO> betaReaderChooseGenres(@NotBlank @RequestParam String piId) {
        Task task = taskService.createTaskQuery().processInstanceId(piId).list().get(0);
        FormFieldsDTO formFieldsDTO = camundaService.getFormFields(task.getProcessInstanceId(), task.getId());
        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "RREG",
                String.format("User from IP address %s submitted Choose Beta Readers form.", ipAddressProvider.get())));
        return new ResponseEntity<>(camundaService.setEnumValues(formFieldsDTO), HttpStatus.OK);
    }

    @Autowired
    public ReaderController(CamundaService camundaService, TaskService taskService, LogService logService,
                            IPAddressProvider ipAddressProvider) {
        this.camundaService = camundaService;
        this.taskService = taskService;
        this.logService = logService;
        this.ipAddressProvider = ipAddressProvider;
    }
}
