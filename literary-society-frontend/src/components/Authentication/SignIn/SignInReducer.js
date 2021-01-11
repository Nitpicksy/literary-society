import * as actionTypes from './SignInActionTypes';

const initialState = {
    accessToken: null,
    expiresIn: null,
    refreshToken: null,
    error: null,
    authRedirectPath: null,
    isAuthenticated: false,
    refreshTokenRequestSent: false, 
    role: null
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.SIGN_IN_START:
            return {
                ...state,
                error: null
            };
        case actionTypes.SIGN_IN_SUCCESS:
            return {
                ...state,
                accessToken: action.userTokenState.accessToken,
                expiresIn: action.userTokenState.expiresIn,
                refreshToken: action.userTokenState.refreshToken,
                role: action.role,
                error: null,
                isAuthenticated: true
            };
        case actionTypes.SIGN_IN_FAIL:
            return {
                ...state,
                error: action.error,
                isAuthenticated: false, 
            };
        case actionTypes.SIGN_OUT:
            return {
                ...state,
                accessToken: null,
                expiresIn: null,
                refreshToken: null,
                isAuthenticated: false,
                authRedirectPath: action.path, 
                role: null
            };
        case actionTypes.SET_SIGN_IN_REDIRECT_PATH:
            return {
                ...state,
                authRedirectPath: action.path
            };
        case actionTypes.REFRESH_TOKEN_START:
            return {
                ...state,
                refreshTokenRequestSent: true
            };
        case actionTypes.REFRESH_TOKEN_SUCCESS:
            return {
                ...state,
                accessToken: action.userTokenState.accessToken,
                expiresIn: action.userTokenState.expiresIn,
                refreshToken: action.userTokenState.refreshToken,
                refreshTokenRequestSent: false
            };
        case actionTypes.REFRESH_TOKEN_FAIL:
            return {
                ...state,
                refreshTokenRequestSent: false
            };
        default:
            return state;
    }
};

export default reducer;