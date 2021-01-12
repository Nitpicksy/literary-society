import axios from '../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './ChoosePaymentMethodsActionTypes';

export const fetchPaymentMethodsSuccess = (paymentMethods, supportedMethods) => {
    return {
        type: actionTypes.FETCH_ALL_AND_SUPPORTED_PAYMENT_METHODS_SUCCESS,
        paymentMethods: paymentMethods,
        supportedMethods:supportedMethods
    };
};

export const fetchPaymentMethodsFail = () => {
    return {
        type: actionTypes.FETCH_ALL_AND_SUPPORTED_PAYMENT_METHODS_FAIL
    };
};

export const fetchPaymentMethods = (token) => {
    return dispatch => {
        axios.get(`/payment-methods/all-and-supported?token=${token}`)
            .then(response => {
                dispatch(fetchPaymentMethodsSuccess(response.data.paymentMethods,response.data.supportedMethods));
            })
            .catch(err => {
                if (err.response) {
                    dispatch(fetchPaymentMethodsFail());
                    toastr.error('Available payment methods', err.response.data.message);
                } else {
                    toastr.error('Available payment methods', 'Something went wrong. Please try again.');
                }
            });
    };
};

export const choosePaymentMethods = (paymentMethods,token) => {
    return dispatch => {
        axios.put(`/payment-methods/company?token=${token}`, paymentMethods)
            .then(response => {
                if (response.data) {
                    toastr.success('Support Payment Methods','Success');
                    window.location.href = response.data;
                }
            })
            .catch(err => {
                toastr.error('Support Payment Methods', err.response.data.message);
            })
    };
};