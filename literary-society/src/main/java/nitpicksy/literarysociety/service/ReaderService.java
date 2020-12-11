package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.model.Reader;
import nitpicksy.literarysociety.model.User;
import org.camunda.bpm.engine.delegate.DelegateExecution;

import java.security.NoSuchAlgorithmException;

public interface ReaderService {

    Reader create(Reader reader, DelegateExecution execution) throws NoSuchAlgorithmException;
}
