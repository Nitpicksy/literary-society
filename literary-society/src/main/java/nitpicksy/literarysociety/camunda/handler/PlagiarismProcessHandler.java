package nitpicksy.literarysociety.camunda.handler;

import nitpicksy.literarysociety.model.Writer;
import nitpicksy.literarysociety.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlagiarismProcessHandler implements ExecutionListener {

    private UserService userService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {

        Writer writer = (Writer) userService.getAuthenticatedUser();
        execution.setVariable("writer", writer.getUsername());
        
    }

    @Autowired
    public PlagiarismProcessHandler(UserService userService) {
        this.userService = userService;
    }
}
