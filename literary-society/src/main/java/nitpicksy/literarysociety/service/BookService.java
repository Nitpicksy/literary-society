package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.model.Book;

import java.util.List;
import java.util.Set;

public interface BookService {

    List<Book> findAllForSale();

    Set<Book> findByIds(List<Long> ids);
}
