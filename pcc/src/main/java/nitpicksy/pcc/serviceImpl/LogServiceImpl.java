package nitpicksy.pcc.serviceImpl;

import nitpicksy.pcc.config.ApplicationConfiguration;
import nitpicksy.pcc.model.Log;
import nitpicksy.pcc.repository.FileRepository;
import nitpicksy.pcc.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;

@Service
public class LogServiceImpl implements LogService {

    private FileRepository<Log> repository;

    private ApplicationConfiguration configuration;

    public void write(Log log) {
        System.out.println(log.toFile());
        try {
            repository.write(Paths.get(configuration.getLogStorage()), log);
        } catch (IOException e) {
            System.out.println("Cannot write log message to a file.");
        }
    }

    @Autowired
    public LogServiceImpl(FileRepository<Log> repository, ApplicationConfiguration configuration) {
        this.repository = repository;
        this.configuration = configuration;
    }

}