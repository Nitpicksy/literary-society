import * as actionTypes from './SignInActionTypes';

const initialState = {
    userTokenState: null,
    error: null,
    authRedirectPath: '/'
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
                userTokenState: action.userTokenState,
                error: null
            };
        case actionTypes.SIGN_IN_FAIL:
            return {
                ...state,
                error: action.error
            };
        case actionTypes.SIGN_OUT:
            return {
                ...state,
                userTokenState: null
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