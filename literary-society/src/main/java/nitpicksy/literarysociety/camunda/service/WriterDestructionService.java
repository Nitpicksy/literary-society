package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.enumeration.UserStatus;
import nitpicksy.literarysociety.model.Log;
import nitpicksy.literarysociety.model.Writer;
import nitpicksy.literarysociety.repository.WriterRepository;
import nitpicksy.literarysociety.service.LogService;
import nitpicksy.literarysociety.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class WriterDestructionService implements JavaDelegate {

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private UserService userService;
    private WriterRepository writerRepository;

    private LogService logService;

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
        Writer savedWriter = writerRepository.save(writer);

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ADDW",
                String.format("Writer registration request %s rejected",savedWriter.getId())));

    }

    @Autowired
    public WriterDestructionService(UserService userService, WriterRepository writerRepository,LogService logService) {
        this.userService = userService;
        this.writerRepository = writerRepository;
        this.logService = logService;
    }
}
