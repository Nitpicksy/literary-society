import axios from '../../../axios-endpoint';

import * as actionTypes from './ChangePasswordActionTypes';
import * as signInActions from '../SignIn/SignInActions';

export const changePasswordStart = () => {
    return {
        type: actionTypes.CHANGE_PASSWORD_START
    };
};

export const changePasswordSuccess = () => {
    return {
        type: actionTypes.CHANGE_PASSWORD_SUCCESS
    };
};

export const changePasswordFail = (error) => {
    return {
        type: actionTypes.CHANGE_PASSWORD_FAIL,
        error: error
    };
};

export const changePassword = (username, oldPassword,newPassword,repeatedPassword,history) => {
    return dispatch => {
        dispatch(changePasswordStart());
        dispatch(signInActions.signOut());

        const authData = {
            username: username,
            oldPassword: oldPassword,
            newPassword:newPassword,
            repeatedPassword:repeatedPassword
        };

        axios.put('/auth', authData)
            .then((response) => {
                console.log("Success")
                history.push('/sign-in')
                dispatch(changePasswordSuccess());
            })
            .catch(err => {
                dispatch(changePasswordFail(err.response.data.message));
            })
    };
};
