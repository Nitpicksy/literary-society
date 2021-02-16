package nitpicksy.literarysociety.elastic.controller;

import nitpicksy.literarysociety.elastic.dto.QueryDTO;
import nitpicksy.literarysociety.elastic.model.BookIndexingUnit;
import nitpicksy.literarysociety.elastic.service.BookIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping(value = "/api/search", produces = MediaType.APPLICATION_JSON_VALUE)
public class SearchController {

    private BookIndexService bookIndexService;

    @PostMapping
    public ResponseEntity<Page<BookIndexingUnit>> search(@RequestBody @NotNull QueryDTO query) {
        if (query.getSearchAllParam() != null && !query.getSearchAllParam().trim().isEmpty()) {
            return new ResponseEntity<>(bookIndexService.allFieldsSearch(query), HttpStatus.OK);
        } else if (query.getQueryParams() != null && !query.getQueryParams().isEmpty()) {
            return new ResponseEntity<>(bookIndexService.combinedSearch(query), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Autowired
    public SearchController(BookIndexService bookIndexService) {
        this.bookIndexService = bookIndexService;
    }
}
