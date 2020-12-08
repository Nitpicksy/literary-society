import * as actionTypes from './SignInActionTypes';

const initialState = {
    accessToken: null,
    expiresIn: null,
    refreshToken: null,
    error: null,
    authRedirectPath: null,
    isAuthenticated: false
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
                error: null,
                isAuthenticated: true
            };
        case actionTypes.SIGN_IN_FAIL:
            return {
                ...state,
                error: action.error,
                isAuthenticated: false
            };
        case actionTypes.SIGN_OUT:
            return {
                ...state,
                accessToken: null,
                expiresIn: null,
                refreshToken: null,
                isAuthenticated: false
            };
        case actionTypes.SET_SIGN_IN_REDIRECT_PATH:
            return {
                ...state,
                authRedirectPath: action.path
            };
        default:
            return state;
    }
};

export default reducer;