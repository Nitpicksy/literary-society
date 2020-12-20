import axios from '../../../axios-endpoint';
import {toastr} from 'react-redux-toastr';
import * as actionTypes from './SignInActionTypes';
import jwt from 'jwt-decode';

export const signInStart = () => {
    return {
        type: actionTypes.SIGN_IN_START
    };
};

export const signInSuccess = (userTokenState) => {
    localStorage.setItem('accessToken', userTokenState.accessToken);
    localStorage.setItem('expiresIn', userTokenState.expiresIn);
    localStorage.setItem('refreshToken', userTokenState.refreshToken);
    const token = jwt(userTokenState.accessToken);
    const role = token.role;
    return {
        type: actionTypes.SIGN_IN_SUCCESS,
        userTokenState: userTokenState, 
        role:role
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
    localStorage.removeItem('shoppingCart');
    return {
        type: actionTypes.SIGN_OUT,
        path: '/sign-in'
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
                if (response.data) {
                    dispatch(setRedirectPath('/'))
                    dispatch(signInSuccess(response.data));
                    toastr.success('Login', 'Success');
                }
            })
            .catch(err => {
                if (err.response.status === 406) {
                    dispatch(setRedirectPath('/change-password'))
                    dispatch(signInFail("You have to change received generic password on first attempt to login."));
                    toastr.error('Login','You have to change received generic password on first attempt to login.');
                } else {
                    toastr.error('Login',err.response.data.message);
                    dispatch(signInFail(err.response.data.message));
                }
            })
    };
};


export const refreshTokenStart = () => {
    return {
        type: actionTypes.REFRESH_TOKEN_START
    };
};

export const refreshTokenSuccess = (userTokenState) => {
    localStorage.setItem('accessToken', userTokenState.accessToken);
    localStorage.setItem('expiresIn', userTokenState.expiresIn);
    localStorage.setItem('refreshToken', userTokenState.refreshToken);
    return {
        type: actionTypes.REFRESH_TOKEN_SUCCESS,
        userTokenState: userTokenState
    };
};

export const refreshTokenFail = (error) => {
    return {
        type: actionTypes.REFRESH_TOKEN_FAIL,
        error: error
    };
};

export const refreshToken = (history) => {
    return dispatch => {
        dispatch(refreshTokenStart());

        axios.post('/auth/refresh')
            .then(response => {
                if (response.data) {
                    window.location.reload();
                    dispatch(refreshTokenSuccess(response.data));
                }
            })
            .catch(err => {
                history.push('/sign-in')
                localStorage.removeItem('accessToken');
                localStorage.removeItem('expiresIn');
                localStorage.removeItem('refreshToken');
                dispatch(refreshTokenFail(err.response.data.message));
            })
    };
};


export const authCheckState = () => {
    return  dispatch => {
        const accessToken = localStorage.getItem('accessToken');
        const expiresIn = localStorage.getItem('expiresIn');
        const refreshToken = localStorage.getItem('refreshToken');
        if(!accessToken) {
            dispatch(signOut());
        }else {
            const userTokenState = {
                accessToken: accessToken,
                expiresIn: expiresIn,
                refreshToken:refreshToken
            };
            dispatch(signInSuccess(userTokenState));   
        }
    };
};