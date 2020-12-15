package nitpicksy.literarysociety.serviceimpl;

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
        return bookRepository.findAllByStatusAndPublishingInfoMerchantSupportsPaymentMethods(
                BookStatus.IN_STORES, true);
    }

    @Override
    public Set<Book> findByIds(List<Long> ids) {
        return bookRepository.findByIdIn(ids);
    }

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
}
