package nitpicksy.literarysociety2.service;

import nitpicksy.literarysociety2.dto.camunda.PublicationRequestDTO;
import nitpicksy.literarysociety2.dto.request.CreateBookRequestDTO;
import nitpicksy.literarysociety2.model.Book;
import nitpicksy.literarysociety2.model.Merchant;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface BookService {

    Book save(Book book);

    List<Book> findAllForSale();

    Set<Book> findByIds(List<Long> ids);

    PublicationRequestDTO getPublicationRequest(Long id);

    Book findById(Long id);

    List<Book> findPublicationRequestsForWriter();
    
    Boolean validatePlagiarismRequest(String bookTitle, String writerName);

    List<Book> getMerchantBooks(Long merchantId);

    Book createBook(CreateBookRequestDTO createBookDTO, Merchant merchant, MultipartFile image);
}
