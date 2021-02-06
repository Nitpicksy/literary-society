package nitpicksy.literarysociety2.controller;

import nitpicksy.literarysociety2.camunda.service.CamundaService;
import nitpicksy.literarysociety2.dto.camunda.EditorsCommentsDTO;
import nitpicksy.literarysociety2.dto.camunda.PlagiarismDetailsDTO;
import nitpicksy.literarysociety2.dto.camunda.PublicationRequestDTO;
import nitpicksy.literarysociety2.dto.camunda.TaskDataDTO;
import nitpicksy.literarysociety2.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety2.exceptionHandler.InvalidDataException;
import nitpicksy.literarysociety2.model.*;
import nitpicksy.literarysociety2.service.*;
import nitpicksy.literarysociety2.utils.IPAddressProvider;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/tasks")
public class TaskController {

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private UserService userService;

    private CamundaService camundaService;

    private BookService bookService;

    private PDFDocumentService pdfDocumentService;

    private TaskService taskService;

    private RuntimeService runtimeService;

    private FormService formService;

    private ImageService imageService;

    private PlagiarismComplaintService plagiarismComplaintService;

    private OpinionOfEditorAboutComplaintService opinionOfEditorAboutComplaintService;

    private static String IMAGES_PATH = "literary-society/src/main/resources/images/";

    private LogService logService;

    private IPAddressProvider ipAddressProvider;

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

            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "DOWB",
                    String.format("User from IP address %s completed task %s and downloaded book", ipAddressProvider.get(), taskId)));
            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } catch (IOException | URISyntaxException e) {
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "DOWB",
                    String.format("Book %s cannot be downloaded. Something went wrong.", pdfDocument.getBook().getId())));
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
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "UPLB",
                    String.format("Book %s cannot be uploaded. Something went wrong.", book.getId())));
            throw new InvalidDataException("Manuscript could not be uploaded. Please try again later.", HttpStatus.BAD_REQUEST);
        }

        camundaService.completeTask(taskId);

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "UPLB",
                String.format("User from IP address %s completed task %s and uploaded book", ipAddressProvider.get(), taskId)));
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping(value = "{taskId}/writer-membership-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> writerMembershipUpload(@NotNull @RequestParam String piId, @NotNull @PathVariable String taskId,
                                                       @Valid @RequestParam MultipartFile[] files) {

        Writer writer = (Writer) userService.getAuthenticatedUser();

        try {
            Set<PDFDocument> uploadedPDFDocuments = new HashSet<>();

            for (MultipartFile file : files) {
                PDFDocument upload = pdfDocumentService.upload(file, null);
                uploadedPDFDocuments.add(upload);
            }

            writer.setAttempts(writer.getAttempts() + 1);
            writer.getDrafts().addAll(uploadedPDFDocuments);

        } catch (IOException e) {
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "UPLM",
                    "Manuscripts %s cannot be uploaded. Something went wrong."));
            throw new InvalidDataException("Writer documents could not be uploaded. Please try again.", HttpStatus.BAD_REQUEST);
        }

        camundaService.completeTask(taskId);

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "UPLM",
                String.format("Writer %s completed task %s and uploaded manuscripts", writer.getId(), taskId)));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "{taskId}/committee", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDataDTO> getTaskDataCommittee(@NotNull @RequestParam String piId, @NotNull @PathVariable String taskId) {
        TaskDataDTO taskDataDTO = new TaskDataDTO(camundaService.getFormFields(piId, taskId),
                pdfDocumentService.getDraftsByWriter(camundaService.getProcessVariable(piId, "writer")));

        return new ResponseEntity<>(taskDataDTO, HttpStatus.OK);
    }


    @GetMapping(value = "/{taskId}/editors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDataDTO> getTaskDataForEditor(@NotNull @RequestParam String piId, @NotNull @PathVariable String taskId) {

        PlagiarismComplaint plagiarismComplaint;

        try {
            Long id = Long.valueOf(camundaService.getProcessVariable(piId, "plagiarismId"));
            plagiarismComplaint = plagiarismComplaintService.findById(id);
        } catch (Exception e) {
            plagiarismComplaint = null;
        }

        List<EditorsCommentsDTO> editorsCommentsDTOS = new ArrayList<>();
        if (plagiarismComplaint != null) {
            editorsCommentsDTOS = opinionOfEditorAboutComplaintService.findOpinionsByPlagiarismComplaint(plagiarismComplaint.getId());
        }

        TaskDataDTO taskDataDTO = new TaskDataDTO(camundaService.setEnumValues(camundaService.getFormFields(piId, taskId)),
                new PlagiarismDetailsDTO(camundaService.getProcessVariable(piId, "writersName"),
                        camundaService.getProcessVariable(piId, "title"),
                        camundaService.getProcessVariable(piId, "mainEditor"),
                        (plagiarismComplaint != null ? new PublicationRequestDTO(plagiarismComplaint.getWritersBook().getId(),
                                plagiarismComplaint.getWritersBook().getTitle(),
                                plagiarismComplaint.getWritersBook().getGenre().getName(),
                                plagiarismComplaint.getWritersBook().getSynopsis()) : null)),
                editorsCommentsDTOS);

        return new ResponseEntity<>(taskDataDTO, HttpStatus.OK);
    }

    @PutMapping(value = "/{taskId}/download-plagiarism", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Transactional
    public ResponseEntity<byte[]> completeTaskAndDownloadPlagiarismBooks(@NotNull @RequestParam String piId, @NotNull @PathVariable String taskId,
                                                                         @NotNull @RequestParam String type) {

        PlagiarismComplaint plagiarismComplaint;
        PDFDocument document;

        String result = camundaService.getProcessVariable(piId, "downloaded");
        if (result != null) {
            camundaService.completeTask(taskId);
        }

        try {
            Long id = Long.valueOf(camundaService.getProcessVariable(piId, "plagiarismId"));
            plagiarismComplaint = plagiarismComplaintService.findById(id);
        } catch (Exception e) {
            plagiarismComplaint = null;
        }

        if (type.equals("SUBMITTED")) {
            document = pdfDocumentService.findByBookId(plagiarismComplaint.getWritersBook().getId());
        } else {
            document = pdfDocumentService.findByBookId(plagiarismComplaint.getReportedBook().getId());
        }

        if (document == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        try {
            headers.setContentDispositionFormData(document.getName(), document.getName());
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            byte[] pdfBytes = pdfDocumentService.download(document.getName());
            camundaService.setProcessVariable(piId, "downloaded", "true");

            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "DOWB",
                    String.format("User from IP address %s completed task %s and downloaded plagiarism books", ipAddressProvider.get(), taskId)));
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IOException | URISyntaxException e) {
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "DOWB",
                    "Plagiarism books %s cannot be downloaded. Something went wrong."));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "{taskId}/membership")
    public ResponseEntity<Void> payMembership(@RequestParam(required = false) String piId, @NotNull @PathVariable String taskId) {
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
        Image savedImage = imageService.saveImage(image, IMAGES_PATH, book);
        book.setImage(savedImage);
        bookService.save(book);

        runtimeService.setVariable(processInstanceId, "formData", formDTOList);
        formService.submitTaskForm(taskId, fieldsMap);

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "DOWB",
                String.format("Editor %s completed task %s.", book.getEditor().getId(), taskId)));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Autowired
    public TaskController(UserService userService, CamundaService camundaService, BookService bookService,
                          PDFDocumentService pdfDocumentService, TaskService taskService, RuntimeService runtimeService, FormService formService,
                          ImageService imageService, PlagiarismComplaintService plagiarismComplaintService,
                          OpinionOfEditorAboutComplaintService opinionOfEditorAboutComplaintService,
                          LogService logService, IPAddressProvider ipAddressProvider) {
        this.userService = userService;
        this.camundaService = camundaService;
        this.bookService = bookService;
        this.pdfDocumentService = pdfDocumentService;
        this.taskService = taskService;
        this.runtimeService = runtimeService;
        this.formService = formService;
        this.imageService = imageService;
        this.plagiarismComplaintService = plagiarismComplaintService;
        this.opinionOfEditorAboutComplaintService = opinionOfEditorAboutComplaintService;
        this.logService = logService;
        this.ipAddressProvider = ipAddressProvider;
    }
}
