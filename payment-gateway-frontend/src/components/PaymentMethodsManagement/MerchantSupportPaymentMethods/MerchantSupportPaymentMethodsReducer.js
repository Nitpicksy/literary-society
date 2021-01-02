import * as actionTypes from './MerchantSupportPaymentMethodsActionTypes';

const initialState = {
    paymentMethods: null,
    error: '',
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_PAYMENT_DATA_SUCCESS:
            return {
                ...state,
                paymentMethods: action.paymentMethods,
                error: null
            };
        case actionTypes.FETCH_PAYMENT_DATA_FAIL:
            return {
                ...state,
                error: action.error
            };
        default:
            return state;
    }
};

export default reducer;