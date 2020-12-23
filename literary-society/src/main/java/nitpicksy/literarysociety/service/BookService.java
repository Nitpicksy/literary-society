package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.dto.camunda.PublicationRequestDTO;
import nitpicksy.literarysociety.model.Book;

import java.util.List;
import java.util.Set;

public interface BookService {

    Book save(Book book);

    List<Book> findAllForSale();

    Set<Book> findByIds(List<Long> ids);

    PublicationRequestDTO getPublicationRequest(Long id);

    Book findById(Long id);
}
