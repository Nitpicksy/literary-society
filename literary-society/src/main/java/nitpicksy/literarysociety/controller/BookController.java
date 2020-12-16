package nitpicksy.literarysociety.controller;

import nitpicksy.literarysociety.camunda.service.CamundaService;
import nitpicksy.literarysociety.constants.CamundaConstants;
import nitpicksy.literarysociety.dto.response.BookDTO;
import nitpicksy.literarysociety.dto.response.FormFieldsDTO;
import nitpicksy.literarysociety.mapper.BookDtoMapper;
import nitpicksy.literarysociety.service.BookService;
import org.camunda.bpm.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/books")
public class BookController {

    private BookService bookService;

    private BookDtoMapper bookDtoMapper;

    private CamundaService camundaService;

    private TaskService taskService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookDTO>> getAllForSale() {
        List<BookDTO> dtoList = bookService.findAllForSale().stream().map(bookDtoMapper::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/start-publishing")
    public ResponseEntity<FormFieldsDTO> getPublicationRequestFields() {
        FormFieldsDTO formFieldsDTO = camundaService.start(CamundaConstants.PROCESS_BOOK_PUBLISHING);
        return new ResponseEntity<>(camundaService.setEnumValues(formFieldsDTO), HttpStatus.OK);
    }

    @Autowired
    public BookController(BookService bookService, BookDtoMapper bookDtoMapper, CamundaService camundaService, TaskService taskService) {
        this.bookService = bookService;
        this.bookDtoMapper = bookDtoMapper;
        this.camundaService = camundaService;
        this.taskService = taskService;
    }
}
