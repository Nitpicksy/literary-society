package nitpicksy.literarysociety.controller;

import nitpicksy.literarysociety.camunda.service.CamundaService;
import nitpicksy.literarysociety.constants.CamundaConstants;
import nitpicksy.literarysociety.dto.camunda.WriterDocumentDTO;
import nitpicksy.literarysociety.dto.response.FormFieldsDTO;
import nitpicksy.literarysociety.dto.response.ProcessDataDTO;
import nitpicksy.literarysociety.service.PDFDocumentService;
import nitpicksy.literarysociety.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(value = "/api/writers", produces = MediaType.APPLICATION_JSON_VALUE)
public class WriterController {

    private CamundaService camundaService;
    private PDFDocumentService pdfDocumentService;
    private UserService userService;

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

    @GetMapping("/drafts")
    public ResponseEntity<List<WriterDocumentDTO>> getDrafts() {
        return new ResponseEntity<>(pdfDocumentService.getDraftsByWriter(userService.getAuthenticatedUser().getUsername()), HttpStatus.OK);
    }

    @Autowired
    public WriterController(CamundaService camundaService, PDFDocumentService pdfDocumentService, UserService userService) {
        this.camundaService = camundaService;
        this.pdfDocumentService = pdfDocumentService;
        this.userService = userService;
    }
}
