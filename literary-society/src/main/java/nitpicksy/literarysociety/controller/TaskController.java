package nitpicksy.literarysociety.controller;

import nitpicksy.literarysociety.camunda.service.CamundaService;
import nitpicksy.literarysociety.dto.camunda.TaskDataDTO;
import nitpicksy.literarysociety.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety.enumeration.BookStatus;
import nitpicksy.literarysociety.exceptionHandler.InvalidDataException;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.Image;
import nitpicksy.literarysociety.model.PDFDocument;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.service.BookService;
import nitpicksy.literarysociety.service.ImageService;
import nitpicksy.literarysociety.service.PDFDocumentService;
import nitpicksy.literarysociety.service.UserService;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.rest.dto.task.TaskDto;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.Path;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/tasks")
public class TaskController {

    private UserService userService;

    private CamundaService camundaService;

    private BookService bookService;

    private PDFDocumentService pdfDocumentService;

    private TaskService taskService;

    private RuntimeService runtimeService;

    private FormService formService;

    private ImageService imageService;

    private static String IMAGES_PATH = "literary-society/src/main/resources/images/";

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskDto>> getTasksForAssignee() {
        User user = userService.getAuthenticatedUser();
        return new ResponseEntity<>(camundaService.getTasksByAssignee(user.getId()), HttpStatus.OK);
    }

    @GetMapping(value = "/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDataDTO> getTaskData(@NotNull @RequestParam String piId, @NotNull @PathVariable String taskId) {

        TaskDataDTO taskDataDTO = new TaskDataDTO(camundaService.setEnumValues(camundaService.getFormFields(piId, taskId)),
                bookService.getPublicationRequest(Long.valueOf(camundaService.getProcessVariable(piId, "bookId"))));

        return new ResponseEntity<>(taskDataDTO, HttpStatus.OK);
    }

    @PutMapping(value = "/{taskId}/complete-and-download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> completeTaskAndDownloadBook(@NotNull @RequestParam String piId, @NotNull @PathVariable String taskId) {
        PDFDocument pdfDocument = pdfDocumentService.findByBookId(Long.valueOf(camundaService.getProcessVariable(piId, "bookId")));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        try {
            headers.setContentDispositionFormData(pdfDocument.getName(), pdfDocument.getName());
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            byte[] content = pdfDocumentService.download(pdfDocument.getName());
            camundaService.completeTask(taskId);
            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } catch (IOException | URISyntaxException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/{taskId}/complete-and-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> completeTaskAndUploadBook(@NotNull @RequestParam String piId, @NotNull @PathVariable String taskId,
                                                          @Valid @RequestParam MultipartFile pdfFile) {
        Book book = bookService.findById(Long.valueOf(camundaService.getProcessVariable(piId, "bookId")));

        try {
            pdfDocumentService.upload(pdfFile, book);
        } catch (IOException e) {
            throw new InvalidDataException("Manuscript could not be uploaded. Please try again later.", HttpStatus.BAD_REQUEST);
        }

//        book.setStatus(BookStatus.SENT);
//        bookService.save(book);

        camundaService.completeTask(taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/{taskId}/submit-form-and-upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<Void> submitFormAndUploadImage(@PathVariable String taskId,
                                                         @RequestPart @Valid List<FormSubmissionDTO> formDTOList, @RequestPart @NotNull MultipartFile image) {

        Map<String, Object> fieldsMap = formDTOList.stream()
                .collect(Collectors.toMap(FormSubmissionDTO::getFieldId, FormSubmissionDTO::getFieldValue));

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        Book book = bookService.findById(Long.valueOf(camundaService.getProcessVariable(processInstanceId, "bookId")));
        Image savedImage = imageService.saveImage(image,IMAGES_PATH,book);
        book.setImage(savedImage);
        bookService.save(book);

        runtimeService.setVariable(processInstanceId, "formData", formDTOList);
        formService.submitTaskForm(taskId, fieldsMap);


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Autowired
    public TaskController(UserService userService, CamundaService camundaService, BookService bookService,
                          PDFDocumentService pdfDocumentService,TaskService taskService,RuntimeService runtimeService, FormService formService,
                          ImageService imageService) {
        this.userService = userService;
        this.camundaService = camundaService;
        this.bookService = bookService;
        this.pdfDocumentService = pdfDocumentService;
        this.taskService = taskService;
        this.runtimeService = runtimeService;
        this.formService = formService;
        this.imageService = imageService;
    }
}
