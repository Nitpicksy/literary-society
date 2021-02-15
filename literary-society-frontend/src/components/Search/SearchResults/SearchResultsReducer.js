import * as actionTypes from './SearchResultsActionTypes';

const initialState = {
    results: null
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.SEARCH_SUCCESS:
            return {
                ...state,
                results: action.results,
            };
        default:
            return state;
    }
};

export default reducer;