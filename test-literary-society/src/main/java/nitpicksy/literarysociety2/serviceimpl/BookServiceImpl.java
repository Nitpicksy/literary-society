package nitpicksy.literarysociety2.serviceimpl;

import nitpicksy.literarysociety2.dto.camunda.PublicationRequestDTO;
import nitpicksy.literarysociety2.dto.request.CreateBookRequestDTO;
import nitpicksy.literarysociety2.enumeration.BookStatus;
import nitpicksy.literarysociety2.model.Book;
import nitpicksy.literarysociety2.model.Image;
import nitpicksy.literarysociety2.model.Merchant;
import nitpicksy.literarysociety2.model.PublishingInfo;
import nitpicksy.literarysociety2.model.Writer;
import nitpicksy.literarysociety2.repository.BookRepository;
import nitpicksy.literarysociety2.repository.PublishingInfoRepository;
import nitpicksy.literarysociety2.service.BookService;
import nitpicksy.literarysociety2.service.GenreService;
import nitpicksy.literarysociety2.service.ImageService;
import nitpicksy.literarysociety2.service.UserService;
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
