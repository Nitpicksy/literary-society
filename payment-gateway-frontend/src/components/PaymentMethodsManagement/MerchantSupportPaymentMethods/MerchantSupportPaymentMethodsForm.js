import { Typography } from '@material-ui/core';
import React from 'react'
import Input from '../../../UI/Input/Input';
import { checkValidity } from '../../../utility/checkValidity';
import { useStyles } from './MerchantSupportPaymentMethodsStyles';

const MerchantSupportPaymentMethodsForm = (props) => {
    let paymentMethodId = null;
    const classes = useStyles();

    const inputChangedHandler = (event, controlName) => {
        let errorMessage;
        let value = event.target.value;

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

    var form = formElementsArray.map(formElement => {
        let title = null;
        if (!paymentMethodId || paymentMethodId !== formElement.config.paymentMethod.id) {
            paymentMethodId = formElement.config.paymentMethod.id;
            title = <Typography component="h1" variant="h5" className = {classes.titlePaymentMethod}>
                {formElement.config.paymentMethod.name}
            </Typography>;
        }
        return (
            <React.Fragment key={formElement.id}>
                {title}
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
                    paymentMethod={formElement.config.paymentMethod}
                    changed={(event) => inputChangedHandler(event, formElement.id)} />
            </React.Fragment>
        );

    });

    return (
        <div>
            {form}
        </div>
    );

}

export default MerchantSupportPaymentMethodsForm;