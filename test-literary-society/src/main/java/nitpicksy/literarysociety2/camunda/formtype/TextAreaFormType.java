package nitpicksy.literarysociety2.camunda.formtype;

import org.camunda.bpm.engine.impl.form.type.SimpleFormFieldType;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.StringValue;
import org.camunda.bpm.engine.variable.value.TypedValue;

public class TextAreaFormType extends SimpleFormFieldType{
    public static final String TYPE_NAME = "textarea";

    public TextAreaFormType() {
    }

    public String getName() {
        return "textarea";
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
