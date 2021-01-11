import * as actionTypes from './MerchantAccountListActionTypes';

const initialState = {
    merchants: null,
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_MERCHANTS_SUCCESS:
            return {
                ...state,
                merchants: action.merchants,
            };
        case actionTypes.FETCH_MERCHANTS_FAIL:
            return {
                ...state,
                merchants: null,
            };
        default:
            return state;
    }
};

export default reducer;