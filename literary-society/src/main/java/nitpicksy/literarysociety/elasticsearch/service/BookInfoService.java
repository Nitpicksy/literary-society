package nitpicksy.literarysociety.elasticsearch.service;

import nitpicksy.literarysociety.elasticsearch.model.BookInfo;
import nitpicksy.literarysociety.model.Book;

public interface BookInfoService {

    BookInfo index(Book book);
}
