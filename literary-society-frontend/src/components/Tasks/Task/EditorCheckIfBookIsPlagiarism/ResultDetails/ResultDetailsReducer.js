import * as actionTypes from './ResultDetailsActionTypes';

const initialState = {
    plagiarismInfo: null,
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_RESULT_SUCCESS:
            return {
                ...state,
                plagiarismInfo: action.plagiarismInfo,
            };
        case actionTypes.FETCH_RESULT_ERROR:
            return {
                ...state,
                plagiarismInfo: null,
            };
        case actionTypes.FETCH_RESULT_START:
            return {
                ...state,
                plagiarismInfo: null,
            };
        default:
            return state;
    }
};

export default reducer;