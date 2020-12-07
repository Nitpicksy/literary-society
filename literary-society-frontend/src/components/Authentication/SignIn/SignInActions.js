import axios from '../../../axios-endpoint';

import * as actionTypes from './SignInActionTypes';


export const signInStart = () => {
    return {
        type: actionTypes.SIGN_IN_START
    };
};

export const signInSuccess = (userTokenState) => {
    return {
        type: actionTypes.SIGN_IN_SUCCESS,
        userTokenState: userTokenState
    };
};

export const signInFail = (error) => {
    return {
        type: actionTypes.SIGN_IN_FAIL,
        error: error
    };
};

export const signOut = () => {
    localStorage.removeItem('userTokenState');
    return {
        type: actionTypes.SIGN_OUT
    };
}

export const setRedirectPath = (path) => {
    return {
        type: actionTypes.SET_SIGN_IN_REDIRECT_PATH, 
        path:path
    };
};

export const signIn = (username,password) => {
    return dispatch => {
        dispatch(signInStart());
        const authData = {
            username:username, 
            password:password
        };

        axios.post('/auth/sign-in',authData)
            .then(response => {
                localStorage.setItem('userTokenState',response.data);
                dispatch(signInSuccess(response.data));
            })
            .catch(err => {
                dispatch(signInFail(err.response.data.error)); 
            })
    };
};

export const logout = () => {
    localStorage.removeItem('userTokenState');
    return {
        type: actionTypes.LOGOUT
    };
}
