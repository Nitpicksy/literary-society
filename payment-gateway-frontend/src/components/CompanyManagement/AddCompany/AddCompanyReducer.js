import * as actionTypes from './AddCompanyActionTypes';

const initialState = {
    paymentMethods: null
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_PAYMENT_METHODS_SUCCESS:
            return {
                ...state,
                paymentMethods: action.paymentMethods,
            };
        case actionTypes.FETCH_PAYMENT_METHODS_FAIL:
            return {
                ...state,
                paymentMethods: null,
            };
        default:
            return state;
    }
};

export default reducer;