export const checkValidity = (name, value, rules, type, patternErrorMessage) => {
    if (!rules) {
        return { isValid: true, errorMessage: '' };
    }

    let reqiredFulfilled = true;
    let isValid = true;
    let errorMessage = name + ' ';

    if (rules.required) {
        reqiredFulfilled = value.trim() !== '';
    } else if (rules.requiredSelect && type === 'selectMultiple') {
        reqiredFulfilled = Array.isArray(value) && value.length;
    }

    if (!reqiredFulfilled) {
        isValid = false;
        errorMessage = name + ' field is required.';
    } else {
        if (rules.minlength) {
            isValid = value.length >= rules.minlength;
            if (!isValid) {
                errorMessage = 'Minimal length for ' + name.toLowerCase() + ' is ' + rules.minlength + '.';
            }
        }

        if (isValid && rules.maxlength) {
            isValid = value.length <= rules.maxlength;
            if (!isValid) {
                errorMessage = 'Maximal length for ' + name.toLowerCase() + ' is ' + rules.maxlength + '.';
            }
        }

        if (isValid && rules.isNumeric) {
            const pattern = /^\d+$/;
            isValid = pattern.test(value);
            if (!isValid) {
                errorMessage = 'Entered data is not a number.';
            }
        }

        if (isValid && rules.pattern) {
            const pattern = new RegExp(rules.pattern);
            isValid = pattern.test(value)
            if (!isValid) {
                errorMessage = patternErrorMessage;
            }
        }
    }

    return { isValid: isValid, errorMessage: errorMessage };
}
