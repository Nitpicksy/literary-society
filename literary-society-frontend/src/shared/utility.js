export const checkValidity = (value, rules,label) => {
    let isValid = true;
    if (!rules) {
        return true;
    }

    let errorMessage = label + ' ';
    if (rules.required) {
        isValid = value.trim() !== '' && isValid;
        if(!isValid){
            errorMessage += 'is required and ';
        }
    }

    if (rules.minLength) {
        isValid = value.length >= rules.minLength && isValid
        if(!isValid){
            errorMessage += 'min length is ' + value.length + ' ';
        }
    }

    if (rules.maxLength) {
        isValid = value.length <= rules.maxLength && isValid
        if(!isValid){
            errorMessage += 'max length is ' + value.length + ' ';
        }
    }

    if (rules.isEmail) {
        const pattern = /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/;
        isValid = pattern.test(value) && isValid
        if(!isValid){
            errorMessage += 'is not valid ' +' ';
        }
    }

    if (rules.isNumeric) {
        const pattern = /^\d+$/;
        isValid = pattern.test(value) && isValid
        if(!isValid){
            errorMessage += 'is not numeric ' +' ';
        }
    }

    if(rules.isPassword){
        const pattern = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[_#?!@$%^&*-.,:;]).{10,64}$/;
        isValid = pattern.test(value) && isValid
        if(!isValid){
            errorMessage += 'must be between 10 and 64 characters long and must contain a number, a special character, a lowercase and an uppercase letter';
        }
    }
    return {isValid: isValid, errorMessage: errorMessage};
}
