package nitpicksy.literarysociety.controller;

import nitpicksy.literarysociety.camunda.service.CamundaService;
import nitpicksy.literarysociety.constants.CamundaConstants;
import nitpicksy.literarysociety.constants.RoleConstants;
import nitpicksy.literarysociety.dto.response.*;
import nitpicksy.literarysociety.enumeration.UserStatus;
import nitpicksy.literarysociety.exceptionHandler.InvalidTokenException;
import nitpicksy.literarysociety.mapper.*;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.BuyerToken;
import nitpicksy.literarysociety.model.PDFDocument;
import nitpicksy.literarysociety.model.Reader;
import nitpicksy.literarysociety.model.Transaction;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.model.VerificationToken;
import nitpicksy.literarysociety.service.BookService;
import nitpicksy.literarysociety.service.BuyerTokenService;
import nitpicksy.literarysociety.service.OpinionOfBetaReaderService;
import nitpicksy.literarysociety.service.OpinionOfEditorService;
import nitpicksy.literarysociety.service.PDFDocumentService;
import nitpicksy.literarysociety.service.ReaderService;
import nitpicksy.literarysociety.service.UserService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping(value = "/api/books")
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

    private BuyerTokenService buyerTokenService;

    private PDFDocumentService pdfDocumentService;

    private UserService userService;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookDTO>> getAllForSale() {
        List<BookDTO> dtoList = bookService.findAllForSale().stream().map(bookDtoMapper::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping(value = "/publication-requests",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PublicationRequestResponseDTO>> getPublicationRequestsForWriter() {
        return new ResponseEntity<>(bookService.findPublicationRequestsForWriter().stream()
                .map(publReqResponseDtoMapper::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "/start-publishing",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProcessDataDTO> getPublicationRequestFields() {
        ProcessDataDTO processDataDTO = camundaService.start(CamundaConstants.PROCESS_BOOK_PUBLISHING);
        return new ResponseEntity<>(processDataDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/publication-request-form",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FormFieldsDTO> getPublicationRequestForm(@NotNull @RequestParam String piId, @NotNull @RequestParam String taskId) {
        FormFieldsDTO formFieldsDTO = camundaService.getFormFields(piId, taskId);
        return new ResponseEntity<>(camundaService.setEnumValues(formFieldsDTO), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookDetailsDTO> getBookDetails(@Positive @PathVariable Long id) {
        Book book = bookService.findById(id);
        return new ResponseEntity<>(bookDetailsDtoMapper.toDto(book), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/opinions-of-beta-readers",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OpinionDTO>> getOpinionsOfBetaReaders(@Positive @PathVariable Long id) {
        return new ResponseEntity<>(opinionOfBetaReaderService.findByBookId(id).stream()
                .map(opinion -> opinionOfBetaReaderDtoMapper.toDto(opinion)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/opinion-of-editor",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OpinionDTO> getOpinionOfEditor(@Positive @PathVariable Long id) {
        return new ResponseEntity<>(opinionOfEditorDtoMapper.toDto(opinionOfEditorService.findNewestByBookId(id)), HttpStatus.OK);
    }

    @GetMapping(value = "/download",produces="application/zip")
    public void downloadBooks(@RequestParam String t, HttpServletResponse response) throws IOException {
        BuyerToken buyerToken = buyerTokenService.verifyToken(t);

        if (buyerToken == null) {
            throw new InvalidTokenException("This token is invalid or expired.", HttpStatus.BAD_REQUEST);
        }
        Transaction transaction = buyerToken.getTransaction();
        List<PDFDocument> pdfDocumentList = pdfDocumentService.findByBooks(transaction.getOrderedBooks());

        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=books.zip");
        ZipOutputStream zipOutputStream = null;
        try{
            zipOutputStream = new ZipOutputStream(response.getOutputStream());
            for (PDFDocument pdfDocument : pdfDocumentList) {
                File file = pdfDocumentService.download(pdfDocument);
                zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                FileInputStream fileInputStream = new FileInputStream(file);

                IOUtils.copy(fileInputStream, zipOutputStream);

                fileInputStream.close();
                zipOutputStream.closeEntry();
            }

        } catch (IOException e) {
        }finally {
            if(zipOutputStream != null){
                zipOutputStream.flush();
                zipOutputStream.close();
            }
        }

        buyerTokenService.invalidateToken(buyerToken.getId());
    }

    @GetMapping(value = "/download/{bookId}",produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadBook(@NotNull @Positive @PathVariable Long bookId) {
        Book book = bookService.findById(bookId);

        if(book == null) {
            throw new InvalidTokenException("This book doesn't exist.", HttpStatus.BAD_REQUEST);
        }

        if(book.getPublishingInfo().getPrice() != 0){
            Reader reader = userService.getAuthenticatedReader();
            if(reader == null || !reader.getPurchasedBooks().contains(book)){
                throw new InvalidTokenException("This book is not free so you have to buy it.", HttpStatus.BAD_REQUEST);
            }
        }

        PDFDocument pdfDocument = pdfDocumentService.findByBookId(bookId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        try {
            headers.setContentDispositionFormData(pdfDocument.getName(), pdfDocument.getName());
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return new ResponseEntity<>(pdfDocumentService.download(pdfDocument.getName()), headers, HttpStatus.OK);
        } catch (IOException | URISyntaxException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Autowired
    public BookController(BookService bookService, OpinionOfBetaReaderService opinionOfBetaReaderService,
                          OpinionOfEditorService opinionOfEditorService, CamundaService camundaService, BookDtoMapper bookDtoMapper,
                          BookDetailsDtoMapper bookDetailsDtoMapper, OpinionOfBetaReaderDtoMapper opinionOfBetaReaderDtoMapper,
                          OpinionOfEditorDtoMapper opinionOfEditorDtoMapper, PublicationRequestResponseDtoMapper publReqResponseDtoMapper,
                          BuyerTokenService buyerTokenService,PDFDocumentService pdfDocumentService,UserService userService) {
        this.bookService = bookService;
        this.opinionOfBetaReaderService = opinionOfBetaReaderService;
        this.opinionOfEditorService = opinionOfEditorService;
        this.camundaService = camundaService;
        this.bookDtoMapper = bookDtoMapper;
        this.bookDetailsDtoMapper = bookDetailsDtoMapper;
        this.opinionOfBetaReaderDtoMapper = opinionOfBetaReaderDtoMapper;
        this.opinionOfEditorDtoMapper = opinionOfEditorDtoMapper;
        this.publReqResponseDtoMapper = publReqResponseDtoMapper;
        this.buyerTokenService = buyerTokenService;
        this.pdfDocumentService = pdfDocumentService;
        this.userService = userService;
    }
}
