package nitpicksy.literarysociety2.controller;

import nitpicksy.literarysociety2.camunda.service.CamundaService;
import nitpicksy.literarysociety2.constants.CamundaConstants;
import nitpicksy.literarysociety2.dto.camunda.WriterDocumentDTO;
import nitpicksy.literarysociety2.dto.response.FormFieldsDTO;
import nitpicksy.literarysociety2.dto.response.ProcessDataDTO;
import nitpicksy.literarysociety2.model.Log;
import nitpicksy.literarysociety2.service.LogService;
import nitpicksy.literarysociety2.service.PDFDocumentService;
import nitpicksy.literarysociety2.service.UserService;
import nitpicksy.literarysociety2.utils.IPAddressProvider;
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

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private CamundaService camundaService;
    private PDFDocumentService pdfDocumentService;
    private UserService userService;

    private LogService logService;

    private IPAddressProvider ipAddressProvider;

    @GetMapping("/start-registration")
    public ResponseEntity<ProcessDataDTO> startProcess() {
        ProcessDataDTO processDataDTO = camundaService.start(CamundaConstants.PROCESS_WRITER_REGISTRATION);
        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "WREG",
                String.format("User from IP address %s started writer's registration process.",ipAddressProvider.get())));
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

    @GetMapping("/start-plagiarism")
    public ResponseEntity<ProcessDataDTO> startPlagiarismProcess() {
        ProcessDataDTO processDataDTO = camundaService.start(CamundaConstants.PROCESS_PLAGIARISM);
        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "PLAGP",
                String.format("User from IP address %s started plagiarism process.",ipAddressProvider.get())));
        return new ResponseEntity<>(processDataDTO, HttpStatus.OK);
    }


    @Autowired
    public WriterController(CamundaService camundaService, PDFDocumentService pdfDocumentService, UserService userService,
                            LogService logService, IPAddressProvider ipAddressProvider) {
        this.camundaService = camundaService;
        this.pdfDocumentService = pdfDocumentService;
        this.userService = userService;
        this.logService = logService;
        this.ipAddressProvider = ipAddressProvider;
    }
}
