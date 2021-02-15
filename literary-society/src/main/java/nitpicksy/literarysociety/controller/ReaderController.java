package nitpicksy.literarysociety.controller;

import nitpicksy.literarysociety.camunda.service.CamundaService;
import nitpicksy.literarysociety.constants.CamundaConstants;
import nitpicksy.literarysociety.dto.camunda.EnumKeyValueDTO;
import nitpicksy.literarysociety.dto.response.FormFieldsDTO;
import nitpicksy.literarysociety.dto.response.ProcessDataDTO;
import nitpicksy.literarysociety.elasticsearch.model.ReaderInfo;
import nitpicksy.literarysociety.elasticsearch.service.SearchService;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.Log;
import nitpicksy.literarysociety.model.Reader;
import nitpicksy.literarysociety.model.Writer;
import nitpicksy.literarysociety.service.BookService;
import nitpicksy.literarysociety.service.LogService;
import nitpicksy.literarysociety.service.ReaderService;
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
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;

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

    private BookService bookService;

    private SearchService searchService;

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

    @GetMapping("/beta/filter")
    public ResponseEntity<List<EnumKeyValueDTO>> filterBetaReaders(@NotBlank @RequestParam String piId,@NotNull @RequestParam Boolean filter) {
        Long bookId = Long.valueOf(camundaService.getProcessVariable(piId, "bookId"));
        Book book = bookService.findById(bookId);
        Writer writer = book.getWriter();
        List<ReaderInfo> readerInfos;

        if(filter){
            readerInfos = searchService.filterBetaReaders(writer.getLocation().getLat(), writer.getLocation().getLon(),
                    book.getGenre().getName());
        }else{
            readerInfos = searchService.findBetaReaders(book.getGenre().getName());
        }
        return new ResponseEntity<>(convert(readerInfos), HttpStatus.OK);
    }

    private List<EnumKeyValueDTO> convert(List<ReaderInfo> readerInfos){
        List<EnumKeyValueDTO> enumList = new ArrayList<>();
        for (ReaderInfo reader : readerInfos) {
            String key = "id_" + reader.getId();
            String readerInfo = reader.getName()  + "  from " + reader.getCity() + " (" + reader.getCountry() + ")";
            enumList.add(new EnumKeyValueDTO(key,readerInfo));
        }
        return enumList;
    }
    @Autowired
    public ReaderController(CamundaService camundaService, TaskService taskService, LogService logService,
                            IPAddressProvider ipAddressProvider,BookService bookService,SearchService searchService) {
        this.camundaService = camundaService;
        this.taskService = taskService;
        this.logService = logService;
        this.ipAddressProvider = ipAddressProvider;
        this.bookService = bookService;
        this.searchService = searchService;
    }
}
