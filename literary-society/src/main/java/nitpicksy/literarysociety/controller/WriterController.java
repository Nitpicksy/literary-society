package nitpicksy.literarysociety.controller;

import nitpicksy.literarysociety.camunda.service.CamundaService;
import nitpicksy.literarysociety.constants.CamundaConstants;
import nitpicksy.literarysociety.dto.response.FormFieldsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/writers", produces = MediaType.APPLICATION_JSON_VALUE)
public class WriterController {

    private CamundaService camundaService;

    @GetMapping("/start-registration")
    public ResponseEntity<FormFieldsDTO> getRegistrationFields() {
        FormFieldsDTO formFieldsDTO = camundaService.start(CamundaConstants.PROCESS_WRITER_REGISTRATION);
        return new ResponseEntity<>(camundaService.setEnumValues(formFieldsDTO), HttpStatus.OK);
    }

    @Autowired
    public WriterController(CamundaService camundaService) {
        this.camundaService = camundaService;
    }
}
