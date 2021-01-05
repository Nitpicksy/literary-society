package nitpicksy.literarysociety.camunda.handler;

import nitpicksy.literarysociety.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MembershipPaymentHandler implements ExecutionListener {

    private UserService userService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String username = (String) execution.getVariable("writer");
        execution.setVariable("user", username);
        execution.setVariable("success", false);
    }

    @Autowired
    public MembershipPaymentHandler(UserService userService) {
        this.userService = userService;
    }
}