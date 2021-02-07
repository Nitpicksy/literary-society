import * as actionTypes from './ChangePasswordActionTypes';

const initialState = {
    error: null,
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.CHANGE_PASSWORD_START:
            return {
                ...state,
                error: null
            };
        case actionTypes.CHANGE_PASSWORD_SUCCESS:
            return {
                ...state,
                error: null,
            };
        case actionTypes.CHANGE_PASSWORD_FAIL:
            return {
                ...state,
                error: action.error,
            };
        default:
            return state;
    }
};

export default reducer;