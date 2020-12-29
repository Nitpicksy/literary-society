import axios from '../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './PaymentMethodListActionTypes';

export const fetchPaymentMethodsSuccess = (paymentMethods) => {
    return {
        type: actionTypes.FETCH_PAYMENT_METHODS_SUCCESS,
        paymentMethods: paymentMethods
    };
};

export const fetchPaymentMethodsFail = () => {
    return {
        type: actionTypes.FETCH_PAYMENT_METHODS_FAIL
    };
};

export const fetchPaymentMethods = () => {
    return dispatch => {
        axios.get('/payment-methods')
            .then(response => {
                dispatch(fetchPaymentMethodsSuccess(response.data));
            })
            .catch(err => {
                dispatch(fetchPaymentMethodsFail());
                toastr.error('Payment Methods', 'Something went wrong. Please try again.');
            });
    };
};


export const changePaymentMethodStatus = (id, status) => {
    return dispatch => {
        axios.put(`/payment-methods/${id}?status=${status}`)
            .then(() => {
                dispatch(fetchPaymentMethods())
                toastr.success('Payment Methods', 'Successfully ' + status + "ed payment method's request.");
            })
            .catch(() => {
                toastr.error('Payment Methods', 'Something went wrong. Please try again.');
            });
    }
};