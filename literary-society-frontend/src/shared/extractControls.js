export const extractControls = (formFields) => {
    const controlsMap = new Map();
    for (let field of formFields) {
        let control = extractControl(field);
        controlsMap.set(field.id, control[field.id]);
    }
    return Object.fromEntries(controlsMap);
}

const extractControl = (field) => {
    let control = null;
    switch (field.type.name) {
        case ('string'):
            control = {
                [field.id]: {
                    elementType: 'input',
                    elementConfig: {
                        label: field.label,
                    },
                    value: '',
                    validation: extractConstraints(field.validationConstraints),
                    error: false,
                    errorMessage: '',
                    additionalData: {
                        ...field.properties
                    }
                }
            };
            break;
        case ('textarea'):
            control = {
                [field.id]: {
                    elementType: 'textarea',
                    elementConfig: {
                        label: field.label,
                        rows: field.properties.rows
                    },
                    value: '',
                    validation: extractConstraints(field.validationConstraints),
                    error: false,
                    errorMessage: '',
                    additionalData: {
                        ...field.properties
                    }
                }
            };
            break;
        case ('password'):
            control = {
                [field.id]: {
                    elementType: 'input',
                    elementConfig: {
                        type: 'password',
                        label: field.label,
                        ...field.properties,
                    },
                    value: '',
                    validation: extractConstraints(field.validationConstraints),
                    error: false,
                    errorMessage: '',
                    additionalData: {
                        ...field.properties
                    }
                }
            };
            break;
        case ('boolean'):
            let defaultValue = false;
            if(field.defaultValue){
                defaultValue = field.defaultValue;
            }
            control = {
                [field.id]: {
                    elementType: 'checkbox',
                    elementConfig: {
                        label: field.label,
                        ...field.properties,
                    },
                    value: defaultValue,
                }
            };
            break;
        default:
            control = {
                [field.id]: {
                    elementType: 'input',
                    elementConfig: {
                        label: field.label,
                        ...extractConstraints(field.properties),
                        ...field.properties,
                    },
                    value: '',
                    validation: extractConstraints(field.validationConstraints),
                    error: false,
                    errorMessage: '',
                    additionalData: {
                        ...field.properties
                    }
                }
            };
    }
    return control;
}

const extractConstraints = (validationConstraints) => {
    let map = new Map();
    for (let i in validationConstraints) {
        map.set(validationConstraints[i].name, validationConstraints[i].configuration);
    }
    return Object.fromEntries(map);
}