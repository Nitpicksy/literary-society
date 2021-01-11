import * as actionTypes from './ResetPasswordActionTypes';

const initialState = {
    confirmed: false,
    error: null,
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.REQUEST_TOKEN_START:
            return {
                ...state,
                error: null,
                confirmed: false,
            };
        case actionTypes.REQUEST_TOKEN_SUCCESS:
            return {
                ...state,
                error: null,
                confirmed: true
            };
        case actionTypes.REQUEST_TOKEN_FAIL:
            return {
                ...state,
                error: true,
            };
        case actionTypes.RESET_PASSWORD_START:
            return {
                ...state,
                error: null,
            };
        case actionTypes.RESET_PASSWORD_SUCCESS:
            return {
                ...state,
                error: null,
            };
        case actionTypes.RESET_PASSWORD_FAIL:
            return {
                ...state,
                error: true,
            };
        default:
            return state;
    }
};

export default reducer;