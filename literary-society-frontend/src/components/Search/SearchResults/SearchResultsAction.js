import * as actionTypes from './SearchResultsActionTypes';

export const searchSuccess = (results) => {
    return {
        type: actionTypes.SEARCH_SUCCESS,
        results: results
    };
};