package nitpicksy.literarysociety.camunda.formtype;

import org.camunda.bpm.engine.impl.form.type.SimpleFormFieldType;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.StringValue;
import org.camunda.bpm.engine.variable.value.TypedValue;

public class PasswordFormType extends SimpleFormFieldType {
    public static final String TYPE_NAME = "password";

    public PasswordFormType() {
    }

    public String getName() {
        return "password";
    }

    public TypedValue convertValue(TypedValue propertyValue) {
        if (propertyValue instanceof StringValue) {
            return propertyValue;
        } else {
            Object value = propertyValue.getValue();
            return value == null ? Variables.stringValue((String)null, propertyValue.isTransient()) : Variables.stringValue(value.toString(), propertyValue.isTransient());
        }
    }

    public Object convertFormValueToModelValue(Object propertyValue) {
        return propertyValue.toString();
    }

    public String convertModelValueToFormValue(Object modelValue) {
        return (String)modelValue;
    }
}
