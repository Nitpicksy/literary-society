package nitpicksy.literarysociety2.config;

import nitpicksy.literarysociety2.camunda.formtype.PasswordFormType;
import nitpicksy.literarysociety2.camunda.formtype.TextAreaFormType;
import nitpicksy.literarysociety2.camunda.validator.PatternValidator;
import nitpicksy.literarysociety2.camunda.validator.RequiredSelectValidator;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.impl.AbstractCamundaConfiguration;
import org.springframework.stereotype.Component;

@Component
public class CamundaConfig extends AbstractCamundaConfiguration {

    @Override
    public void preInit(SpringProcessEngineConfiguration config) {
        config.getCustomFormFieldValidators().put("pattern", PatternValidator.class);
        config.getCustomFormFieldValidators().put("requiredSelect", RequiredSelectValidator.class);
        config.getCustomFormTypes().add(new PasswordFormType());
        config.getCustomFormTypes().add(new TextAreaFormType());
    }
}