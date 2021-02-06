package nitpicksy.literarysociety2.camunda.service;

import nitpicksy.literarysociety2.model.Reader;
import nitpicksy.literarysociety2.repository.ReaderRepository;
import nitpicksy.literarysociety2.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PenaltyService implements JavaDelegate {

    private UserService userService;

    private ReaderRepository readerRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String betaReaderUsername = (String) execution.getVariable("betaReader");
        Reader betaReader = (Reader) userService.findByUsername(betaReaderUsername);

        Integer updatedPenalty = betaReader.getPenalty() + 1;
        betaReader.setPenalty(updatedPenalty);
        readerRepository.save(betaReader);

        execution.setVariable("penalty", updatedPenalty);
    }

    @Autowired
    public PenaltyService(UserService userService, ReaderRepository readerRepository) {
        this.userService = userService;
        this.readerRepository = readerRepository;
    }
    
}