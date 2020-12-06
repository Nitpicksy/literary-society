export const extractControls = (formFields) => {
    const controlsMap = new Map();
    for (let field in formFields) {
        control = extractControl(field);
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
                        label: field.label
                    },
                    value: '',
                    validation: extractProperties(field.constraints),
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
                        ...extractProperties(field.properties),
                    },
                    value: '',
                    validation: extractProperties(field.constraints),
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
                    validation: extractProperties(field.constraints),
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
                        ...extractProperties(field.properties),
                    },
                    value: '',
                    validation: extractProperties(field.constraints),
                    error: false,
                    errorMessage: '',
                }
            };
    }
    return control;
}

const extractProperties = (properties) =>  {
    let map = new Map();
    for (let property in properties) {
        map.set(property.name, property.value);
    }

    return Object.fromEntries(map);
}