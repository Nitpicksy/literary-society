package nitpicksy.literarysociety.controller;

import nitpicksy.literarysociety.constants.CamundaConstants;
import nitpicksy.literarysociety.dto.response.FormFieldsDTO;
import nitpicksy.literarysociety.service.CamundaService;
import nitpicksy.literarysociety.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/readers", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReaderController {

    private CamundaService camundaService;

    private ReaderService readerService;

    @GetMapping("/start-registration")
    public ResponseEntity<FormFieldsDTO> get() {
        return new ResponseEntity<>(camundaService.start(CamundaConstants.PROCESS_READER_REGISTRATION), HttpStatus.OK);
    }

    @Autowired
    public ReaderController(CamundaService camundaService, ReaderService readerService) {
        this.camundaService = camundaService;
        this.readerService = readerService;
    }
}
