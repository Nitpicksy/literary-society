export const extractControls = (formFields) => {
    const controlsMap = new Map();
    for (let field of formFields) {
        let control = extractControl(field);
        controlsMap.set(field.id, control[field.id]);
    }
    console.log(Object.fromEntries(controlsMap))
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
                        label: field.label
                    },
                    value: '',
                    validation: extractConstraints(field.validationConstraints),
                    error: false,
                    errorMessage: '',
                }
            };
            break;
        case ('textarea'):
            control = {
                [field.id]: {
                    elementType: 'textarea',
                    elementConfig: {
                        label: field.label,
                        ...field.properties,
                    },
                    value: '',
                    validation: extractConstraints(field.validationConstraints),
                    error: false,
                    errorMessage: '',
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
                    },
                    value: '',
                    validation: extractConstraints(field.validationConstraints),
                    error: false,
                    errorMessage: '',
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
                    },
                    value: '',
                    validation: extractConstraints(field.validationConstraints),
                    error: false,
                    errorMessage: '',
                }
            };
    }
    return control;
}

const extractConstraints = (validationConstraints) =>  {
    let map = new Map();
    for (let validationConstraint in validationConstraints) {
        map.set(validationConstraint.name, validationConstraint.configuration);
    }

    return Object.fromEntries(map);
}