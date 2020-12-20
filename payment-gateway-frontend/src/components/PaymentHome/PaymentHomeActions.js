import axios from "../../axios-endpoint";
import * as actionTypes from "./PaymentHomeActionTypes";
import { toastr } from "react-redux-toastr";

export const fetchPaymentMethodsStart = () => {
  return {
    type: actionTypes.FETCH_PAYMENT_METHODS_START,
  };
};

export const fetchPaymentMethodSuccess = (paymentMethods) => {
  return {
    type: actionTypes.FETCH_PAYMENT_METHODS_END,
    paymentMethods: paymentMethods,
  };
};

export const fetchPaymentDetailsStart = () => {
  return {
    type: actionTypes.FETCH_PAYMENT_DETAILS_START,
  };
};

export const fetchPaymentDetailsSuccess = (paymentDetails) => {
  return {
    type: actionTypes.FETCH_PAYMENT_DETAILS_END,
    paymentDetails: paymentDetails,
  };
};

export const fetchPaymentMethods = (transactionId) => {
  return (dispatch) => {
    dispatch(fetchPaymentMethodsStart());
    axios.get(`/payment-methods/${transactionId}`).then((response) => {
      dispatch(fetchPaymentMethodSuccess(response.data));
    });
  };
};

export const fetchPaymentDetails = (transactionId) => {
  return (dispatch) => {
    dispatch(fetchPaymentDetailsStart());
    axios.get(`/orders/${transactionId}`).then((response) => {
      dispatch(fetchPaymentDetailsSuccess(response.data));
    });
  };
};

export const forwardPayment = (request) => {
  return (dispatch) => {
    axios
      .post(`/orders/forward`, request)
      .then((response) => {
        window.location.href = response.data;
      })
      .catch((err) => {
        toastr.error(
          "Forwarding request to service",
          err.response.data.message
        );
          window.location.href = err.response.data.message;
      });
  };
};
