export const extractControlsFromPaymentMethodsData = (paymentMethods) => {
    const controlsMap = new Map();
    for (let paymentMethod of paymentMethods) {
        for (let paymentData of paymentMethod.listPaymentDataDTO) {
            let control = extractControl(paymentData,paymentMethod.paymentMethodDTO);
            controlsMap.set(paymentData.id, control[paymentData.id]);
        }
    }
    return Object.fromEntries(controlsMap);
}

const extractControl = (field, paymentMethod ) => {
    let control = null;

    switch (field.attributeType) {
        case ('text'):
            control = {
                [field.id]: {
                    elementType: 'input',
                    elementConfig: {
                        label: field.attributeName,
                    },
                    value: '',
                    validation: {
                        required: true,
                    },
                    valid: false,
                    touched: false,
                    error: false,
                    paymentMethod: paymentMethod
                }
            };
            break;
        case ('password'):
            control = {
                [field.id]: {
                    elementType: 'input',
                    elementConfig: {
                        type: 'password',
                        label: field.attributeName,
                    },
                    value: '',
                    validation: {
                        required: true,
                    },
                    valid: false,
                    touched: false,
                    error: false,
                    paymentMethod: paymentMethod
                }
            };
            break;
        default:
            control = {
                [field.id]: {
                    elementType: 'input',
                    elementConfig: {
                        label: field.attributeName,
                    },
                    value: '',
                    validation: {
                        required: true,
                    },
                    valid: false,
                    touched: false,
                    error: false,
                    paymentMethod: paymentMethod
                }
            };
    }
    return control;
}
