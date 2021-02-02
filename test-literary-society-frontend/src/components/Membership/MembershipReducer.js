import * as actionTypes from './MembershipActionTypes';

const initialState = {
    membership: null,
    priceList: null
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_MEMBERSHIP_SUCCESS:
            return {
                ...state,
                membership: action.membership,
            };
        case actionTypes.FETCH_MEMBERSHIP_FAIL:
            return {
                ...state,
                membership: null,
            };
        case actionTypes.FETCH_PRICE_LIST_SUCCESS:
            return {
                ...state,
                priceList: action.priceList,
            };
        case actionTypes.FETCH_PRICE_LIST_FAIL:
            return {
                ...state,
                priceList: null,
            };
        default:
            return state;
    }
};

export default reducer;