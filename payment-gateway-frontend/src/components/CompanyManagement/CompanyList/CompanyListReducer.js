import * as actionTypes from './CompanyListActionTypes';

const initialState = {
    companies: null,
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_COMPANIES_SUCCESS:
            return {
                ...state,
                companies: action.companies,
            };
        case actionTypes.FETCH_COMPANIES_FAIL:
            return {
                ...state,
                companies: null,
            };
        default:
            return state;
    }
};

export default reducer;