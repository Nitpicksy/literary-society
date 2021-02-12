package nitpicksy.literarysociety.controller;

import nitpicksy.literarysociety.camunda.service.CamundaService;
import nitpicksy.literarysociety.constants.CamundaConstants;
import nitpicksy.literarysociety.dto.request.CreateBookRequestDTO;
import nitpicksy.literarysociety.dto.response.*;
import nitpicksy.literarysociety.elasticsearch.dto.SearchParamDTO;
import nitpicksy.literarysociety.elasticsearch.model.BookInfo;
import nitpicksy.literarysociety.elasticsearch.service.BookInfoService;
import nitpicksy.literarysociety.elasticsearch.service.SearchService;
import nitpicksy.literarysociety.exceptionHandler.InvalidDataException;
import nitpicksy.literarysociety.exceptionHandler.InvalidTokenException;
import nitpicksy.literarysociety.mapper.*;
import nitpicksy.literarysociety.model.*;
import nitpicksy.literarysociety.service.*;
import nitpicksy.literarysociety.utils.IPAddressProvider;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Validated
@RestController
@RequestMapping(value = "/api/books")
public class BookController {

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

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

    private MembershipService membershipService;

    private UserService userService;

    private LogService logService;

    private IPAddressProvider ipAddressProvider;

    private BookInfoService bookInfoService;

    private SearchService searchService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookDTO>> getAllForSale() {
        List<BookDTO> dtoList = bookService.findAllForSale().stream().map(bookDtoMapper::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping(value = "/publication-requests", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PublicationRequestResponseDTO>> getPublicationRequestsForWriter() {
        return new ResponseEntity<>(bookService.findPublicationRequestsForWriter().stream()
                .map(publReqResponseDtoMapper::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "/start-publishing", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProcessDataDTO> getPublicationRequestFields() {
        User writer = userService.getAuthenticatedUser();
        if (!membershipService.checkIfUserMembershipIsValid(writer.getUserId())) {
            throw new InvalidDataException("You haven't paid Writer Membership, therefore you can not create publication requests.", HttpStatus.BAD_REQUEST);
        }

        ProcessDataDTO processDataDTO = camundaService.start(CamundaConstants.PROCESS_BOOK_PUBLISHING);
        return new ResponseEntity<>(processDataDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/publication-request-form", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FormFieldsDTO> getPublicationRequestForm(@NotBlank @RequestParam String piId, @NotBlank @RequestParam String taskId) {
        FormFieldsDTO formFieldsDTO = camundaService.getFormFields(piId, taskId);
        return new ResponseEntity<>(camundaService.setEnumValues(formFieldsDTO), HttpStatus.OK);
    }

    @GetMapping(value = "/plagiarism-complaint-form", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FormFieldsDTO> getPlagiarismComplaintForm(@NotBlank @RequestParam String piId, @NotBlank @RequestParam String taskId) {
        FormFieldsDTO formFieldsDTO = camundaService.getFormFields(piId, taskId);
        return new ResponseEntity<>(camundaService.setEnumValues(formFieldsDTO), HttpStatus.OK);
    }

    @GetMapping(value = "/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> validatePlagiarismRequest(@NotBlank @RequestParam String bookTitle, @NotBlank @RequestParam String writerName) {
        return new ResponseEntity<>(bookService.validatePlagiarismRequest(bookTitle, writerName), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookDetailsDTO> getBookDetails(@NotNull @Positive @PathVariable Long id) {
        Book book = bookService.findById(id);
        return new ResponseEntity<>(bookDetailsDtoMapper.toDto(book), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/opinions-of-beta-readers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OpinionDTO>> getOpinionsOfBetaReaders(@NotNull @Positive @PathVariable Long id) {
        return new ResponseEntity<>(opinionOfBetaReaderService.findByBookId(id).stream()
                .map(opinion -> opinionOfBetaReaderDtoMapper.toDto(opinion)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/opinion-of-editor", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OpinionDTO> getOpinionOfEditor(@NotNull @Positive @PathVariable Long id) {
        return new ResponseEntity<>(opinionOfEditorDtoMapper.toDto(opinionOfEditorService.findNewestByBookId(id)), HttpStatus.OK);
    }

    @GetMapping(value = "/download", produces = "application/zip")
    public void downloadBooks(@NotBlank @RequestParam String t, HttpServletResponse response) throws IOException {
        BuyerToken buyerToken = buyerTokenService.verifyToken(t);

        if (buyerToken == null) {
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "BT",
                    String.format("Invalid or expired buyer token provided from %s", ipAddressProvider.get())));
            throw new InvalidTokenException("This token is invalid or expired.", HttpStatus.BAD_REQUEST);
        }
        Transaction transaction = buyerToken.getTransaction();
        List<PDFDocument> pdfDocumentList = pdfDocumentService.findByBooks(transaction.getOrderedBooks());

        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=books.zip");
        ZipOutputStream zipOutputStream = null;
        try {
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
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "BOOK", "Could not create ZipOutputStream."));
        } finally {
            if (zipOutputStream != null) {
                zipOutputStream.flush();
                zipOutputStream.close();
            }
        }

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "BOOK",
                String.format("Transaction %s is successfully created and books are successfully downloaded.", transaction.getId())));
        buyerTokenService.invalidateToken(buyerToken.getId());
    }

    @GetMapping(value = "/download/{bookId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadBook(@NotNull @Positive @PathVariable Long bookId) {
        Book book = bookService.findById(bookId);

        if (book == null) {
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "BOOK",
                    String.format("Book with %s doesn't exist.", bookId)));
            throw new InvalidTokenException("This book doesn't exist.", HttpStatus.BAD_REQUEST);
        }

        if (book.getPublishingInfo().getPrice() != 0) {
            Reader reader = userService.getAuthenticatedReader();
            if (reader == null || !reader.getPurchasedBooks().contains(book)) {
                logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "BT",
                        String.format("User from %s tried to download book %s which is not free and which is not purchased.", ipAddressProvider.get(), bookId)));
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
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "BOOK", String.format("Could not download book %s .", bookId)));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/purchased", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookDTO>> getAllPurchased() {

        Reader reader = userService.getAuthenticatedReader();
        List<BookDTO> dtoList = reader.getPurchasedBooks().stream().map(bookDtoMapper::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping(value = "/merchant", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookDTO>> getMerchantBooks() {
        Merchant merchant = userService.getAuthenticatedMerchant();

        List<BookDTO> dtoList = bookService.getMerchantBooks(merchant.getUserId()).stream().map(bookDtoMapper::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> create(@RequestPart @Valid CreateBookRequestDTO createBookDTO, @RequestPart @NotNull MultipartFile image,
                                       @RequestPart @NotNull MultipartFile pdfFile) throws IOException {
        Merchant merchant = userService.getAuthenticatedMerchant();
        if (!merchant.isSupportsPaymentMethods()) {
            throw new InvalidDataException("You have to support all available payment methods before you start to sell books.", HttpStatus.BAD_REQUEST);
        }

        Book book = bookService.createBook(createBookDTO, merchant, image);
        pdfDocumentService.upload(pdfFile, book);

        bookInfoService.index(book);

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ADDB",
                String.format("Book %s successfully created", book.getId())));
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<BookInfo>> search(@RequestParam @PositiveOrZero int page, @RequestParam @Positive int size,
                                                 @RequestParam(required = false) String searchValue,
                                                 @RequestBody List<SearchParamDTO> searchParams){

        if(!searchValue.isEmpty()) {
            return new ResponseEntity<>(searchService.search(searchValue,page,size), HttpStatus.OK);
        }

        return new ResponseEntity<>(searchService.combineSearchParams(searchParams,page,size), HttpStatus.OK);
    }

    @Autowired
    public BookController(BookService bookService, OpinionOfBetaReaderService opinionOfBetaReaderService,
                          OpinionOfEditorService opinionOfEditorService, CamundaService camundaService, BookDtoMapper bookDtoMapper,
                          BookDetailsDtoMapper bookDetailsDtoMapper, OpinionOfBetaReaderDtoMapper opinionOfBetaReaderDtoMapper,
                          OpinionOfEditorDtoMapper opinionOfEditorDtoMapper, PublicationRequestResponseDtoMapper publReqResponseDtoMapper,
                          BuyerTokenService buyerTokenService, PDFDocumentService pdfDocumentService, UserService userService, LogService logService,
                          IPAddressProvider ipAddressProvider, MembershipService membershipService, BookInfoService bookInfoService,
                          SearchService searchService) {
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
        this.membershipService = membershipService;
        this.userService = userService;
        this.logService = logService;
        this.ipAddressProvider = ipAddressProvider;
        this.bookInfoService = bookInfoService;
        this.searchService = searchService;
    }
}
