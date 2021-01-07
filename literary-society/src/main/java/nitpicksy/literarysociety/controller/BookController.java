package nitpicksy.literarysociety.controller;

import nitpicksy.literarysociety.camunda.service.CamundaService;
import nitpicksy.literarysociety.constants.CamundaConstants;
import nitpicksy.literarysociety.dto.response.*;
import nitpicksy.literarysociety.mapper.*;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.service.BookService;
import nitpicksy.literarysociety.service.OpinionOfBetaReaderService;
import nitpicksy.literarysociety.service.OpinionOfEditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/books", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookController {

    private BookService bookService;

    private OpinionOfBetaReaderService opinionOfBetaReaderService;

    private OpinionOfEditorService opinionOfEditorService;

    private CamundaService camundaService;

    private BookDtoMapper bookDtoMapper;

    private BookDetailsDtoMapper bookDetailsDtoMapper;

    private OpinionOfBetaReaderDtoMapper opinionOfBetaReaderDtoMapper;

    private OpinionOfEditorDtoMapper opinionOfEditorDtoMapper;

    private PublicationRequestResponseDtoMapper publReqResponseDtoMapper;

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllForSale() {
        List<BookDTO> dtoList = bookService.findAllForSale().stream().map(bookDtoMapper::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping(value = "/publication-requests")
    public ResponseEntity<List<PublicationRequestResponseDTO>> getPublicationRequestsForWriter() {
        return new ResponseEntity<>(bookService.findPublicationRequestsForWriter().stream()
                .map(publReqResponseDtoMapper::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/start-publishing")
    public ResponseEntity<ProcessDataDTO> getPublicationRequestFields() {
        ProcessDataDTO processDataDTO = camundaService.start(CamundaConstants.PROCESS_BOOK_PUBLISHING);
        return new ResponseEntity<>(processDataDTO, HttpStatus.OK);
    }

    @GetMapping("/publication-request-form")
    public ResponseEntity<FormFieldsDTO> getPublicationRequestForm(@NotNull @RequestParam String piId, @NotNull @RequestParam String taskId) {
        FormFieldsDTO formFieldsDTO = camundaService.getFormFields(piId, taskId);
        return new ResponseEntity<>(camundaService.setEnumValues(formFieldsDTO), HttpStatus.OK);
    }

    @GetMapping("/plagiarism-complaint-form")
    public ResponseEntity<FormFieldsDTO> getPlagiarismComplaintForm(@NotNull @RequestParam String piId, @NotNull @RequestParam String taskId) {
        FormFieldsDTO formFieldsDTO = camundaService.getFormFields(piId, taskId);
        return new ResponseEntity<>(camundaService.setEnumValues(formFieldsDTO), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<BookDetailsDTO> getBookDetails(@Positive @PathVariable Long id) {
        Book book = bookService.findById(id);
        return new ResponseEntity<>(bookDetailsDtoMapper.toDto(book), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/opinions-of-beta-readers")
    public ResponseEntity<List<OpinionDTO>> getOpinionsOfBetaReaders(@Positive @PathVariable Long id) {
        return new ResponseEntity<>(opinionOfBetaReaderService.findByBookId(id).stream()
                .map(opinion -> opinionOfBetaReaderDtoMapper.toDto(opinion)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/opinion-of-editor")
    public ResponseEntity<OpinionDTO> getOpinionOfEditor(@Positive @PathVariable Long id) {
        return new ResponseEntity<>(opinionOfEditorDtoMapper.toDto(opinionOfEditorService.findNewestByBookId(id)), HttpStatus.OK);
    }

    @Autowired

    public BookController(BookService bookService, OpinionOfBetaReaderService opinionOfBetaReaderService,
                          OpinionOfEditorService opinionOfEditorService, CamundaService camundaService, BookDtoMapper bookDtoMapper,
                          BookDetailsDtoMapper bookDetailsDtoMapper, OpinionOfBetaReaderDtoMapper opinionOfBetaReaderDtoMapper,
                          OpinionOfEditorDtoMapper opinionOfEditorDtoMapper, PublicationRequestResponseDtoMapper publReqResponseDtoMapper) {
        this.bookService = bookService;
        this.opinionOfBetaReaderService = opinionOfBetaReaderService;
        this.opinionOfEditorService = opinionOfEditorService;
        this.camundaService = camundaService;
        this.bookDtoMapper = bookDtoMapper;
        this.bookDetailsDtoMapper = bookDetailsDtoMapper;
        this.opinionOfBetaReaderDtoMapper = opinionOfBetaReaderDtoMapper;
        this.opinionOfEditorDtoMapper = opinionOfEditorDtoMapper;
        this.publReqResponseDtoMapper = publReqResponseDtoMapper;
    }
}
