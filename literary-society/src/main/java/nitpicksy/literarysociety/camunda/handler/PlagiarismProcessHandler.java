package nitpicksy.literarysociety.camunda.handler;

import nitpicksy.literarysociety.enumeration.UserStatus;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.model.Writer;
import nitpicksy.literarysociety.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlagiarismProcessHandler implements ExecutionListener {

    private UserService userService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {

        Writer writer = (Writer) userService.getAuthenticatedUser();
        execution.setVariable("writer", writer.getUsername());

        List<String> committeeMembers = new ArrayList<>();
        List<User> users = userService.findAllWithRoleAndStatus("ROLE_COMMITTEE_MEMBER", UserStatus.ACTIVE);

        users.forEach(user -> committeeMembers.add(user.getUsername()));
        execution.setVariable("committeeMembersList", committeeMembers);
        
    }

    @Autowired
    public PlagiarismProcessHandler(UserService userService) {
        this.userService = userService;
    }
}
