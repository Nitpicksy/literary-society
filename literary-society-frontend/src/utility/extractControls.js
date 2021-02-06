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
    const constaints = extractConstraints(field.validationConstraints);
    let valid = false;
    let touched = false;
    if (isEmpty(constaints)) {
        valid = true
        touched = true
    }
    switch (field.type.name) {
        case ('string'):
            control = {
                [field.id]: {
                    elementType: 'input',
                    elementConfig: {
                        label: field.label,
                    },
                    value: '',
                    validation: constaints,
                    valid: false,
                    touched: false,
                    error: false,
                    errorMessage: '',
                    additionalData: {
                        ...field.properties
                    }
                }
            };
            break;
        case ('file'):
            control = {
                [field.id]: {
                    elementType: 'file',
                    elementConfig: {},
                    value: null,
                    validation: null,
                    valid: true,
                    touched: false,
                    error: false,
                    errorMessage: '',
                    additionalData: {
                        fileName: ''
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
                    valid: valid,
                    touched: touched,
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
                    },
                    value: '',
                    validation: extractConstraints(field.validationConstraints),
                    valid: false,
                    touched: false,
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
            if (field.defaultValue) {
                defaultValue = field.defaultValue;
            }
            control = {
                [field.id]: {
                    elementType: 'checkbox',
                    elementConfig: {
                        label: field.label
                    },
                    value: defaultValue,
                    error: false,
                    valid: true
                }
            };
            break;
        case ('enum'):
            control = {
                [field.id]: {
                    elementType: field.properties.multipleSelect === 'true' ? 'selectMultiple' : 'selectOne',
                    elementConfig: {
                        label: field.label,
                        options: extractOptions(field.type.values),
                    },
                    value: field.properties.multipleSelect === 'true' ? [] : '',
                    validation: extractConstraints(field.validationConstraints),
                    valid: false,
                    touched: true,
                    error: false,
                    errorMessage: '',
                    additionalData: {
                        ...field.properties
                    }
                }
            };
            break;
        case ('long'):
            control = {
                [field.id]: {
                    elementType: 'long',
                    elementConfig: {
                        label: field.label,
                    },
                    value: '',
                    validation: constaints,
                    valid: true,
                    touched: false,
                    error: false,
                    errorMessage: '',
                    additionalData: {
                        ...field.properties
                    }
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
                    valid: false,
                    touched: false,
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

const extractOptions = (options) => {
    let array = [];
    for (let [key, value] of Object.entries(options)) {
        array.push({ value: key, displayValue: value });
    }

    return array;
}

const isEmpty = (obj) => {
    return Object.keys(obj).length === 0;
}