package nitpicksy.literarysociety.elastic.service;

import nitpicksy.literarysociety.elastic.dto.QueryDTO;
import nitpicksy.literarysociety.elastic.model.BookIndexingUnit;
import org.springframework.data.domain.Page;

public interface SearchService {

    Page<BookIndexingUnit> allFieldsSearch(QueryDTO query);

    Page<BookIndexingUnit> combinedSearch(QueryDTO query);

}
