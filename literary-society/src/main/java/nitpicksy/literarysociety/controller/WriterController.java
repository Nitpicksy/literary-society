package nitpicksy.literarysociety.controller;

import nitpicksy.literarysociety.camunda.service.CamundaService;
import nitpicksy.literarysociety.constants.CamundaConstants;
import nitpicksy.literarysociety.dto.response.FormFieldsDTO;
import nitpicksy.literarysociety.dto.response.ProcessDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(value = "/api/writers", produces = MediaType.APPLICATION_JSON_VALUE)
public class WriterController {

    private CamundaService camundaService;

    @GetMapping("/start-registration")
    public ResponseEntity<ProcessDataDTO> startProcess() {
        ProcessDataDTO processDataDTO = camundaService.start(CamundaConstants.PROCESS_WRITER_REGISTRATION);
        return new ResponseEntity<>(processDataDTO, HttpStatus.OK);
    }

    @GetMapping("/registration-form")
    public ResponseEntity<FormFieldsDTO> getRegistrationForm(@NotNull @RequestParam String piId, @NotNull @RequestParam String taskId) {
        FormFieldsDTO formFieldsDTO = camundaService.getFormFields(piId, taskId);
        return new ResponseEntity<>(camundaService.setEnumValues(formFieldsDTO), HttpStatus.OK);
    }

    @Autowired
    public WriterController(CamundaService camundaService) {
        this.camundaService = camundaService;
    }
}
