package nitpicksy.literarysociety.config;

import nitpicksy.literarysociety.camunda.formtype.FileFormType;
import nitpicksy.literarysociety.camunda.formtype.PasswordFormType;
import nitpicksy.literarysociety.camunda.formtype.TextAreaFormType;
import nitpicksy.literarysociety.camunda.validator.PatternValidator;
import nitpicksy.literarysociety.camunda.validator.RequiredSelectValidator;
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
        config.getCustomFormTypes().add(new FileFormType());
    }
}