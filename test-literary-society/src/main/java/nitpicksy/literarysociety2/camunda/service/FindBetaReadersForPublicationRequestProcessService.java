package nitpicksy.literarysociety2.camunda.service;

import nitpicksy.literarysociety2.dto.camunda.EnumKeyValueDTO;
import nitpicksy.literarysociety2.enumeration.UserStatus;
import nitpicksy.literarysociety2.model.Book;
import nitpicksy.literarysociety2.model.Reader;
import nitpicksy.literarysociety2.repository.BookRepository;
import nitpicksy.literarysociety2.repository.ReaderRepository;
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

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long bookId = Long.valueOf((String) execution.getVariable("bookId"));
        Book book = bookRepository.findOneById(bookId);

        List<Reader> betaReaders = readerRepository.findByIsBetaReaderAndBetaReaderGenresIdAndStatus(true, book.getGenre().getId(), UserStatus.ACTIVE);

        List<EnumKeyValueDTO> enumList = new ArrayList<>();
        for (Reader reader : betaReaders) {
            String key = "id_" + reader.getUserId();
            String readerInfo = reader.getFirstName() + " "+ reader.getLastName() + "  from " + reader.getCity();
            enumList.add(new EnumKeyValueDTO(key,readerInfo));
        }
        execution.setVariable("selectBetaReaderList", enumList);
    }

    @Autowired
    public FindBetaReadersForPublicationRequestProcessService(ReaderRepository readerRepository, BookRepository bookRepository) {
        this.readerRepository = readerRepository;
        this.bookRepository = bookRepository;
    }
}
