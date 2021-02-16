package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.dto.camunda.EnumKeyValueDTO;
import nitpicksy.literarysociety.elastic.model.BetaReaderIndexingUnit;
import nitpicksy.literarysociety.elastic.service.BetaReaderIndexService;
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
public class FindBetaReadersForPublicationRequestProcessService implements JavaDelegate {

    private BookRepository bookRepository;

    private BetaReaderIndexService betaReaderIndexService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long bookId = Long.valueOf((String) execution.getVariable("bookId"));
        Book book = bookRepository.findOneById(bookId);

        List<BetaReaderIndexingUnit> betaReaderIdxUnits = betaReaderIndexService.filterByGenre(book.getGenre().getName());

        List<EnumKeyValueDTO> enumDTOList = new ArrayList<>();
        betaReaderIdxUnits.forEach(betaReaderIdxUnit -> {
            String key = "id_" + betaReaderIdxUnit.getId();
            String value = betaReaderIdxUnit.getName() + " (" + betaReaderIdxUnit.getCityAndCountry() + ")";
            enumDTOList.add(new EnumKeyValueDTO(key, value));
        });
        execution.setVariable("selectBetaReaderList", enumDTOList);
    }

    @Autowired
    public FindBetaReadersForPublicationRequestProcessService(BookRepository bookRepository, BetaReaderIndexService betaReaderIndexService) {
        this.bookRepository = bookRepository;
        this.betaReaderIndexService = betaReaderIndexService;
    }
}
