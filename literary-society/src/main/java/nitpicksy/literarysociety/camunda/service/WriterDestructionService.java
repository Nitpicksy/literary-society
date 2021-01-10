package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.enumeration.UserStatus;
import nitpicksy.literarysociety.model.Writer;
import nitpicksy.literarysociety.repository.WriterRepository;
import nitpicksy.literarysociety.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class WriterDestructionService implements JavaDelegate {

    private UserService userService;
    private WriterRepository writerRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        String username = (String) execution.getVariable("writer");
        if (username.isEmpty()) {
            username = (String) execution.getVariable("username");
        }

        Writer writer = (Writer) userService.findByUsername(username);
        writer.setStatus(UserStatus.REJECTED);
        writer.setDrafts(new HashSet<>());
        writer.setGenre(new HashSet<>());
        writerRepository.save(writer);
    }

    @Autowired
    public WriterDestructionService(UserService userService, WriterRepository writerRepository) {
        this.userService = userService;
        this.writerRepository = writerRepository;
    }
}
