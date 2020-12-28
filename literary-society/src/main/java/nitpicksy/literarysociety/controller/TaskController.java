package nitpicksy.literarysociety.controller;

import nitpicksy.literarysociety.camunda.service.CamundaService;
import nitpicksy.literarysociety.constants.CamundaConstants;
import nitpicksy.literarysociety.dto.camunda.TaskDataDTO;
import nitpicksy.literarysociety.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety.dto.response.FormFieldsDTO;
import nitpicksy.literarysociety.enumeration.BookStatus;
import nitpicksy.literarysociety.exceptionHandler.InvalidDataException;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.PDFDocument;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.service.BookService;
import nitpicksy.literarysociety.service.PDFDocumentService;
import nitpicksy.literarysociety.service.UserService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.rest.dto.task.TaskDto;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

@RestController
@RequestMapping(value = "/api/tasks")
public class TaskController {

    private UserService userService;

    private CamundaService camundaService;

    private BookService bookService;

    private PDFDocumentService pdfDocumentService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskDto>> getTasksForAssignee() {
        User user = userService.getAuthenticatedUser();
        return new ResponseEntity<>(camundaService.getTasksByAssignee(user.getId()), HttpStatus.OK);
    }

    @GetMapping(value = "{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDataDTO> getTaskData(@NotNull @RequestParam String piId, @NotNull @PathVariable String taskId) {

        TaskDataDTO taskDataDTO = new TaskDataDTO(camundaService.setEnumValues(camundaService.getFormFields(piId, taskId)),
                bookService.getPublicationRequest(Long.valueOf(camundaService.getProcessVariable(piId, "bookId"))));

        return new ResponseEntity<>(taskDataDTO, HttpStatus.OK);
    }

    @PutMapping(value = "{taskId}/complete-and-download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
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

    @PostMapping(value = "{taskId}/complete-and-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> completeTaskAndUploadBook(@NotNull @RequestParam String piId, @NotNull @PathVariable String taskId,
                                                          @Valid @RequestParam MultipartFile pdfFile) {
        Book book = bookService.findById(Long.valueOf(camundaService.getProcessVariable(piId, "bookId")));

        try {
            pdfDocumentService.upload(pdfFile, book);
        } catch (IOException e) {
            throw new InvalidDataException("Manuscript could not be uploaded. Please try again later.", HttpStatus.BAD_REQUEST);
        }

        book.setStatus(BookStatus.SENT);
        bookService.save(book);

        camundaService.completeTask(taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "upload-proba",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> uploadProba(@Valid @RequestParam MultipartFile pdfFile) {
        try {
            pdfDocumentService.upload(pdfFile, null);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "proba/{name}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> proba(@PathVariable String name) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        try {
            byte[] content = pdfDocumentService.download(name);
            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } catch (IOException | URISyntaxException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Autowired
    public TaskController(UserService userService, CamundaService camundaService, BookService bookService,
                          PDFDocumentService pdfDocumentService) {
        this.userService = userService;
        this.camundaService = camundaService;
        this.bookService = bookService;
        this.pdfDocumentService = pdfDocumentService;
    }
}
