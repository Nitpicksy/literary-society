package nitpicksy.literarysociety.camunda.service;
import nitpicksy.literarysociety.exceptionHandler.InvalidDataException;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
public class ReaderRegistrationExceptionService implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
       String errorMessage = (String) execution.getVariable("errorMessage");
       throw new InvalidDataException(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
