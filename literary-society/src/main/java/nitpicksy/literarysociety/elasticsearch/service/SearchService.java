package nitpicksy.literarysociety.elasticsearch.service;

import nitpicksy.literarysociety.elasticsearch.dto.SearchParamDTO;
import nitpicksy.literarysociety.elasticsearch.model.BookInfo;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SearchService {

    Page<BookInfo> combineSearchParams(List<SearchParamDTO> searchParams, int page, int size);

    Page<BookInfo> search(String searchValue, int page, int size);
}
