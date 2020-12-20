package nitpicksy.literarysociety.camunda.validator;

import org.camunda.bpm.engine.impl.form.validator.FormFieldValidator;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidatorContext;

public class RequiredSelectValidator implements FormFieldValidator {

    @Override
    public boolean validate(Object o, FormFieldValidatorContext formFieldValidatorContext) {
        if (o == null) {
            return true;
        } else if (o instanceof String) {
            String submittedValue = (String) o;
            return !submittedValue.isEmpty() == Boolean.parseBoolean(formFieldValidatorContext.getConfiguration());
        }
        return false;
    }
}
