import { React } from 'react'
import Input from '../Input/Input';
import { checkValidity } from '../../utility/checkValidity';

const Form = (props) => {

    const inputChangedHandler = (event, controlName) => {
        let errorMessage;
        let value = event.target.value;
        if (props.controls[controlName].elementType === 'checkbox') {
            value = event.target.checked;
        } else {
            if (props.controls[controlName].additionalData) {
                errorMessage = props.controls[controlName].additionalData.errorMessage
            }
        }
        let controlToValidate = props.controls[controlName];
        const validationData = checkValidity(controlToValidate.elementConfig.label, value, controlToValidate.validation, controlToValidate.elementType, errorMessage);

        const updatedControls = {
            ...props.controls,
            [controlName]: {
                ...props.controls[controlName],
                value: value,
                error: !validationData.isValid,
                errorMessage: validationData.errorMessage,
                touched: true,
                valid: validationData.isValid
            }
        };

        let formIsValid = true;
        for (let inputId in updatedControls) {
            formIsValid = updatedControls[inputId].valid && formIsValid;
        }
        props.setControls(updatedControls);
        props.setFormIsValid(formIsValid);
    }

    const formElementsArray = [];
    for (let key in props.controls) {
        formElementsArray.push({
            id: key,
            config: props.controls[key]
        });
    }

    var form = formElementsArray.map(formElement => (
        <Input
            key={formElement.id}
            elementType={formElement.config.elementType}
            elementConfig={formElement.config.elementConfig}
            value={formElement.config.value}
            invalid={!formElement.config.valid}
            shouldValidate={formElement.config.validation}
            touched={formElement.config.touched}
            error={formElement.config.error}
            errorMessage={formElement.config.errorMessage}
            additionalData={formElement.config.additionalData}
            changed={(event) => inputChangedHandler(event, formElement.id)} />
    ));

    return (
        <div>
            {form}
        </div>
    );

}

export default Form;