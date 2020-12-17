package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.dto.camunda.PublicationRequestDTO;
import nitpicksy.literarysociety.enumeration.BookStatus;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.repository.BookRepository;
import nitpicksy.literarysociety.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

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
        return new PublicationRequestDTO(book.getId(),book.getTitle(),book.getGenre().getName(),book.getSynopsis());
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findOneById(id);
    }

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
}
