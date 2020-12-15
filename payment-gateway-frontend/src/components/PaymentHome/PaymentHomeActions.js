import axios from "../../axios-endpoint";
import * as actionTypes from "./PaymentHomeActionTypes";

export const fetchPaymentMethodsStart = () => {
  return {
    type: actionTypes.FETCH_PAYMENT_METHODS_START,
  };
};

export const fetchTransactionSuccess = (paymentMethods) => {
  return {
    type: actionTypes.FETCH_PAYMENT_METHODS_END,
    paymentMethods: paymentMethods,
  };
};

export const fetchPaymentMethods = (transactionId) => {
  return (dispatch) => {
    dispatch(fetchPaymentMethodsStart());
    axios.get(`/payment-methods/${transactionId}`).then((response) => {
      dispatch(fetchTransactionSuccess(response.data));
    });
  };
};
