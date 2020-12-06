package nitpicksy.literarysociety.validator;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidator;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidatorContext;

public class PatternValidator implements FormFieldValidator {

    @Override
    public boolean validate(Object o, FormFieldValidatorContext formFieldValidatorContext) {
        if(o instanceof String){
            String submittedValue = (String) o;
            return submittedValue.matches(formFieldValidatorContext.getConfiguration());
        }
        return false;
    }
}
