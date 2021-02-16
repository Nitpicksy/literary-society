package nitpicksy.literarysociety.elasticsearch.service;

import nitpicksy.literarysociety.elasticsearch.model.BookInfo;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.PDFDocument;

public interface BookInfoService {

    BookInfo index(Book book);
}
