import * as actionTypes from './ChoosePaymentMethodsActionTypes';

const initialState = {
    paymentMethods: null, 
    supportedMethods: null
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_ALL_AND_SUPPORTED_PAYMENT_METHODS_SUCCESS:
            return {
                ...state,
                paymentMethods: action.paymentMethods,
                supportedMethods: action.supportedMethods,
            };
        case actionTypes.FETCH_ALL_AND_SUPPORTED_PAYMENT_METHODS_FAIL:
            return {
                ...state,
                paymentMethods: null,
                supportedMethods: null
            };
        default:
            return state;
    }
};

export default reducer;