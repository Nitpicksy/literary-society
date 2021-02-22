package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.dto.camunda.PublicationRequestDTO;
import nitpicksy.literarysociety.dto.request.CreateBookRequestDTO;
import nitpicksy.literarysociety.enumeration.BookStatus;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.Image;
import nitpicksy.literarysociety.model.Merchant;
import nitpicksy.literarysociety.model.PDFDocument;
import nitpicksy.literarysociety.model.PublishingInfo;
import nitpicksy.literarysociety.model.Writer;
import nitpicksy.literarysociety.plagiarist.dto.PaperResultDTO;
import nitpicksy.literarysociety.repository.BookRepository;
import nitpicksy.literarysociety.repository.PublishingInfoRepository;
import nitpicksy.literarysociety.service.BookService;
import nitpicksy.literarysociety.service.GenreService;
import nitpicksy.literarysociety.service.ImageService;
import nitpicksy.literarysociety.service.UserService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class BookServiceImpl implements BookService {

    private static String IMAGES_PATH = "literary-society/src/main/resources/images/";

    private BookRepository bookRepository;

    private UserService userService;

    private GenreService genreService;

    private PublishingInfoRepository publishingInfoRepository;

    private ImageService imageService;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findByStatus(BookStatus status) {
        return bookRepository.findByStatus(status);
    }

    @Override
    public List<Book> findAllForSale() {
        return bookRepository.findByStatusAndPublishingInfoMerchantSupportsPaymentMethods(
                BookStatus.IN_STORES, true);
    }

    @Override
    public Set<Book> findByIds(List<Long> ids) {
        return bookRepository.findByIdIn(ids);
    }

    @Override
    public PublicationRequestDTO getPublicationRequest(Long id) {
        Book book = bookRepository.findOneById(id);
        return new PublicationRequestDTO(book.getId(), book.getTitle(), book.getGenre().getName(), book.getSynopsis());
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findOneById(id);
    }

    @Override
    public List<Book> findPublicationRequestsForWriter() {
        Writer writer = (Writer) userService.getAuthenticatedUser();
        return bookRepository.findByWriterId(writer.getUserId());
    }

    @Override
    public Boolean validatePlagiarismRequest(String bookTitle, String writerName) {
        Book book = bookRepository.findFirstByTitleContainingAndWritersNamesContaining(bookTitle, writerName);
        return book != null;
    }

    @Override
    public List<Book> getMerchantBooks(Long merchantId) {
        return bookRepository.findByStatusAndPublishingInfoMerchantSupportsPaymentMethodsAndPublishingInfoMerchantId(
                BookStatus.IN_STORES, true, merchantId);
    }

    @Override
    @Transactional
    public Book createBook(CreateBookRequestDTO dto, Merchant merchant, MultipartFile image) {
        Book book = new Book(dto.getWritersNames(), dto.getTitle(),dto.getSynopsis(),genreService.findById(dto.getGenre()));
        Book savedBook = bookRepository.saveAndFlush(book);

        PublishingInfo publishingInfo = new PublishingInfo(dto.getISBN(), dto.getNumberOfPages(), dto.getPublisherCity(), getLocalDate(dto.getPublicationDate()), dto.getPublisher(), dto.getPrice(),
                dto.getDiscount(), savedBook, merchant);
        PublishingInfo saved =  publishingInfoRepository.saveAndFlush(publishingInfo);
        savedBook.setPublishingInfo(saved);

        Image savedImage = imageService.saveImage(image, IMAGES_PATH, savedBook);
        savedBook.setImage(savedImage);
        savedBook = bookRepository.saveAndFlush(savedBook);

        return savedBook;
    }


    private LocalDate getLocalDate(String date)  {
        return LocalDate.parse(date);
    }

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, UserService userService, GenreService genreService,
                           PublishingInfoRepository publishingInfoRepository,ImageService imageService) {
        this.bookRepository = bookRepository;
        this.userService = userService;
        this.genreService = genreService;
        this.publishingInfoRepository = publishingInfoRepository;
        this.imageService = imageService;
    }
}
