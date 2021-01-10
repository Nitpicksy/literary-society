package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.dto.camunda.PublicationRequestDTO;
import nitpicksy.literarysociety.enumeration.BookStatus;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.Writer;
import nitpicksy.literarysociety.repository.BookRepository;
import nitpicksy.literarysociety.service.BookService;
import nitpicksy.literarysociety.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    private UserService userService;

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

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, UserService userService) {
        this.bookRepository = bookRepository;
        this.userService = userService;
    }
}
