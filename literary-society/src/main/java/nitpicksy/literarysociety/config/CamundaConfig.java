package nitpicksy.literarysociety.config;

import nitpicksy.literarysociety.validator.EmailFieldValidator;
import nitpicksy.literarysociety.validator.PatternValidator;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.impl.AbstractCamundaConfiguration;
import org.springframework.stereotype.Component;

@Component
public class CamundaConfig extends AbstractCamundaConfiguration {

    @Override
    public void preInit(SpringProcessEngineConfiguration config) {
        config.getCustomFormFieldValidators().put("pattern", PatternValidator.class);
        config.getCustomFormFieldValidators().put("email", EmailFieldValidator.class);
    }
}