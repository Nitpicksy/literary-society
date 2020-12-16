import * as actionTypes from '../SignUp/SignUpActionTypes';

export const setSignUpType = (signUpType) => {
    return {
        type: actionTypes.SIGN_UP_TYPE,
        signUpType: signUpType
    };
};
