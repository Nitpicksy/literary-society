package nitpicksy.literarysociety.elastic.service;

import nitpicksy.literarysociety.elastic.model.BookIndexingUnit;
import nitpicksy.literarysociety.model.Book;

public interface BookIndexService {

    BookIndexingUnit addBook(Book book);

}
