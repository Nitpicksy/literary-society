package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.dto.camunda.PublicationRequestDTO;
import nitpicksy.literarysociety.model.Book;

import java.util.List;

public interface BookService {

    List<Book> findAllForSale();

    PublicationRequestDTO getPublicationRequest(Long id);

}
