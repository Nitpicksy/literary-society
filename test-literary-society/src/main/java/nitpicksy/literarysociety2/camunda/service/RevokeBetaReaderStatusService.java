package nitpicksy.literarysociety2.camunda.service;

import nitpicksy.literarysociety2.model.Reader;
import nitpicksy.literarysociety2.repository.ReaderRepository;
import nitpicksy.literarysociety2.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class RevokeBetaReaderStatusService implements JavaDelegate {

    private UserService userService;

    private ReaderRepository readerRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String betaReaderUsername = (String) execution.getVariable("betaReader");
        Reader betaReader = (Reader) userService.findByUsername(betaReaderUsername);

        betaReader.setBetaReader(false);
        betaReader.setBetaReaderGenres(new HashSet<>());
        readerRepository.save(betaReader);
    }

    @Autowired
    public RevokeBetaReaderStatusService(UserService userService, ReaderRepository readerRepository) {
        this.userService = userService;
        this.readerRepository = readerRepository;
    }
    
}
