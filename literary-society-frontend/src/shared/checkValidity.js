export const checkValidity = (value, rules, name) => {
    let isRequired = false;
    let isValid = true;

    if (!rules) {
        return true;
    }

    let errorMessage = name + ' ';

    if (rules.required) {
        isRequired = value.trim() === '';
        if (isRequired) {
            errorMessage = name + ' is required.';
        }
    }

    if (rules.minLength) {
        isValid = value.length >= rules.minLength && isValid
        if (!isValid && !isRequired) {
            errorMessage = 'Minimal length of ' + name + ' is ' + value.length + '.';
        }
    }

    if (rules.maxLength) {
        isValid = value.length <= rules.maxLength && isValid
        if (!isValid && !isRequired) {
            errorMessage = 'Maximal length of ' + name + ' is ' + value.length + '.';
        }
    }

    if (rules.isEmail) {
        const pattern = /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/;
        isValid = pattern.test(value) && isValid
        if (!isValid && !isRequired) {
            errorMessage = 'Entered data is not an e-mail address.';
        }
    }

    if (rules.isNumeric) {
        const pattern = /^\d+$/;
        isValid = pattern.test(value) && isValid
        if (!isValid && !isRequired) {
            errorMessage = 'Entered data is not a number.';
        }
    }

    if (rules.isUsername) {
        const pattern = /^[A-Za-z0-9]{4,64}$/;
        isValid = pattern.test(value) && isValid
        if (!isValid && !isRequired) {
            errorMessage = name + ' should contain between 4 and 64 alphanumeric characters.';
        }
    }

    if (rules.isPassword) {
        const pattern = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[_#?!@$%^&*-.,:;]).{10,64}$/;
        isValid = pattern.test(value) && isValid
        if (!isValid && !isRequired) {
            errorMessage = name + ' should contain between 10 and 64 characters including 1 number, 1 special character, 1 lowercase and 1 uppercase letter.';
        }
    }

    return { isValid: isValid, errorMessage: errorMessage };
}
