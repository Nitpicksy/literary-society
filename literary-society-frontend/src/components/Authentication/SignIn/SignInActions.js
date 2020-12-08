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
    localStorage.removeItem('accessToken');
    localStorage.removeItem('expiresIn');
    localStorage.removeItem('refreshToken');
    return {
        type: actionTypes.SIGN_OUT
    };
}

export const setRedirectPath = (path) => {
    return {
        type: actionTypes.SET_SIGN_IN_REDIRECT_PATH,
        path: path
    };
};

export const signIn = (username, password) => {
    return dispatch => {
        dispatch(signInStart());
        const authData = {
            username: username,
            password: password
        };

        axios.post('/auth/sign-in', authData)
            .then(response => {
                console.log("success")
                if(response.data){
                    localStorage.setItem('accessToken', response.data.accessToken);
                    localStorage.setItem('expiresIn', response.data.expiresIn);
                    localStorage.setItem('refreshToken', response.data.refreshToken);
                    dispatch(setRedirectPath('/'))
                    dispatch(signInSuccess(response.data));
                }
            })
            .catch(err => {
                if (err.response.status === 406) {
                    dispatch(setRedirectPath('/change-password'))
                    dispatch(signInFail("You have to change received generic password on first attempt to login."));
                } else {
                    dispatch(signInFail(err.response.data.message));
                }
            })
    };
};