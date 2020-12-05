package nitpicksy.literarysociety.validator;

import org.camunda.bpm.engine.impl.form.validator.FormFieldValidator;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;

public class EmailFieldValidator implements FormFieldValidator {

    @Autowired
    private EmailValidator emailValidator;

    @Override
    public boolean validate(Object o, FormFieldValidatorContext formFieldValidatorContext) {
        if(o instanceof String){
            String submittedValue = (String) o;
            return emailValidator.isValid(submittedValue,null);
        }
        return false;
    }
}