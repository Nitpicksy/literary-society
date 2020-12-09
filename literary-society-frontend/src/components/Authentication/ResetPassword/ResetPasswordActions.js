import axios from '../../../axios-endpoint';

import * as actionTypes from './ResetPasswordActionTypes';

export const requestTokenStart = () => {
    return {
        type: actionTypes.REQUEST_TOKEN_START
    };
};

export const requestTokenSuccess = () => {
    return {
        type: actionTypes.REQUEST_TOKEN_SUCCESS
    };
};

export const requestTokenFail = (error) => {
    return {
        type: actionTypes.REQUEST_TOKEN_FAIL,
        error: error
    };
};

export const requestToken = (username) => {
    return dispatch => {
        dispatch(requestTokenStart());
        const authData = {
            username: username
        };

        axios.post('/auth', authData)
            .then(response => {
                dispatch(requestTokenSuccess());
            })
            .catch(err => {
                dispatch(requestTokenFail(err.response.data.message));
            })
    };
};


export const resetPasswordStart = () => {
    return {
        type: actionTypes.RESET_PASSWORD_START
    };
};

export const resetPasswordSuccess = () => {
    return {
        type: actionTypes.RESET_PASSWORD_SUCCESS
    };
};

export const resetPasswordFail = (error) => {
    return {
        type: actionTypes.RESET_PASSWORD_FAIL,
        error: error
    };
};

export const resetPassword = (token, newPassword,repeatedPassword,history) => {
    return dispatch => {
        dispatch(resetPasswordStart());
        const authData = {
            newPassword:newPassword,
            repeatedPassword:repeatedPassword
        };

        axios.put('/auth/reset-password?t='+token, authData)
            .then(response => {
                history.push('/sign-in')
                dispatch(resetPasswordSuccess());
            })
            .catch(err => {
                dispatch(resetPasswordFail(err.response.data.message));
            })
    };
};