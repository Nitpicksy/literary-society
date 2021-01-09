package nitpicksy.literarysociety.camunda.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedirectUserToGatewayService implements JavaDelegate {

    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        String url = (String) execution.getVariable("url");

        simpMessagingTemplate.convertAndSend("/socket-publisher", url);
    }

    @Autowired
    public RedirectUserToGatewayService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }
}
