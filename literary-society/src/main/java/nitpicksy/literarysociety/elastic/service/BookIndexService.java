package nitpicksy.literarysociety.elastic.service;

import nitpicksy.literarysociety.elastic.dto.QueryDTO;
import nitpicksy.literarysociety.elastic.model.BookIndexingUnit;
import nitpicksy.literarysociety.model.Book;
import org.springframework.data.domain.Page;

public interface BookIndexService {

    void addBook(Book book);

    Page<BookIndexingUnit> allFieldsSearch(QueryDTO query);

    Page<BookIndexingUnit> combinedSearch(QueryDTO query);
    
}
