import * as actionTypes from "./PaymentHomeActionTypes";

const initialState = {
  paymentMethods: null,
  paymentDetails: null,
};

const reducer = (state = initialState, action) => {
  switch (action.type) {
    case actionTypes.FETCH_PAYMENT_METHODS_START:
      return {
        ...state,
        paymentMethods: null,
      };
    case actionTypes.FETCH_PAYMENT_METHODS_END:
      return {
        ...state,
        paymentMethods: action.paymentMethods,
      };
    case actionTypes.FETCH_PAYMENT_DETAILS_START:
      return {
        ...state,
        paymentDetails: null,
      };
    case actionTypes.FETCH_PAYMENT_DETAILS_END:
      return {
        ...state,
        paymentDetails: action.paymentDetails,
      };
    default:
      return state;
  }
};

export default reducer;
