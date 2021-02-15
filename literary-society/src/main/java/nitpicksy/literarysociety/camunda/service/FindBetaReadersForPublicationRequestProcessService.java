package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.dto.camunda.EnumKeyValueDTO;
import nitpicksy.literarysociety.elasticsearch.model.ReaderInfo;
import nitpicksy.literarysociety.elasticsearch.service.SearchService;
import nitpicksy.literarysociety.enumeration.UserStatus;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.Reader;
import nitpicksy.literarysociety.repository.BookRepository;
import nitpicksy.literarysociety.repository.ReaderRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FindBetaReadersForPublicationRequestProcessService  implements JavaDelegate {

    private ReaderRepository readerRepository;

    private BookRepository bookRepository;

    private SearchService searchService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long bookId = Long.valueOf((String) execution.getVariable("bookId"));
        Book book = bookRepository.findOneById(bookId);

        List<ReaderInfo>  readerInfos = searchService.findBetaReaders(book.getGenre().getName());

        execution.setVariable("selectBetaReaderList", convert(readerInfos));
    }

    private List<EnumKeyValueDTO> convert(List<ReaderInfo> readerInfos){
        List<EnumKeyValueDTO> enumList = new ArrayList<>();
        for (ReaderInfo reader : readerInfos) {
            String key = "id_" + reader.getId();
            String readerInfo = reader.getName()  + "  from " + reader.getCity() + " (" + reader.getCountry() + ")";
            enumList.add(new EnumKeyValueDTO(key,readerInfo));
        }
        return enumList;
    }

    @Autowired
    public FindBetaReadersForPublicationRequestProcessService(ReaderRepository readerRepository, BookRepository bookRepository,
                                                              SearchService searchService) {
        this.readerRepository = readerRepository;
        this.bookRepository = bookRepository;
        this.searchService = searchService;
    }
}
