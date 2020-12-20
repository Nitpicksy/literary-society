package nitpicksy.literarysociety.controller;

import nitpicksy.literarysociety.camunda.service.CamundaService;
import nitpicksy.literarysociety.constants.CamundaConstants;
import nitpicksy.literarysociety.dto.camunda.TaskDataDTO;
import nitpicksy.literarysociety.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety.dto.response.FormFieldsDTO;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.service.BookService;
import nitpicksy.literarysociety.service.UserService;
import org.camunda.bpm.engine.rest.dto.task.TaskDto;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskController {

    private UserService userService;

    private CamundaService camundaService;

    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<TaskDto>> getTasksForAssignee(){
        User user = userService.getAuthenticatedUser();
        return new ResponseEntity<>(camundaService.getTasksByAssignee(user.getId()), HttpStatus.OK);
    }

    @GetMapping(value="{taskId}")
    public ResponseEntity<TaskDataDTO> getTaskData(@NotNull @RequestParam String piId,@NotNull @PathVariable String taskId) {
        TaskDataDTO taskDataDTO = new TaskDataDTO(camundaService.getFormFields(piId,taskId),
                bookService.getPublicationRequest(Long.valueOf(camundaService.getProcessVariable(piId,"bookId"))));

        return new ResponseEntity<>(taskDataDTO, HttpStatus.OK);
    }

    @Autowired
    public TaskController(UserService userService, CamundaService camundaService,BookService bookService) {
        this.userService = userService;
        this.camundaService = camundaService;
        this.bookService = bookService;
    }
}
