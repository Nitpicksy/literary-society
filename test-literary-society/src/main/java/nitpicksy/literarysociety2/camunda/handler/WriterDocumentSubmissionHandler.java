package nitpicksy.literarysociety2.camunda.handler;

import nitpicksy.literarysociety2.enumeration.UserStatus;
import nitpicksy.literarysociety2.model.User;
import nitpicksy.literarysociety2.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WriterDocumentSubmissionHandler implements ExecutionListener {

    private UserService userService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {

        List<String> committeeMembers = new ArrayList<>();
        List<User> users = userService.findAllWithRoleAndStatus("ROLE_COMMITTEE_MEMBER", UserStatus.ACTIVE);

        users.forEach(user -> committeeMembers.add(user.getUsername()));
        execution.setVariable("committeeMembersList", committeeMembers);
    }

    @Autowired
    public WriterDocumentSubmissionHandler(UserService userService) {
        this.userService = userService;
    }
}
