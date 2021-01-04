package nitpicksy.literarysociety.camunda.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class MembershipPaymentService implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("GUTEN TAG");
        System.out.println("SUCCESS" + execution.getVariable("yes"));
    }
}
