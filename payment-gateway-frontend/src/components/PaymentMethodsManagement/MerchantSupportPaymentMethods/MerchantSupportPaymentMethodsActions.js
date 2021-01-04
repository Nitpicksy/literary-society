import axios from '../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './MerchantSupportPaymentMethodsActionTypes';

export const fetchPaymentDataSuccess = (paymentMethods) => {
    return {
        type: actionTypes.FETCH_PAYMENT_DATA_SUCCESS,
        paymentMethods: paymentMethods,
    };
};

export const fetchPaymentDataFail = (error) => {
    return {
        type: actionTypes.FETCH_PAYMENT_DATA_FAIL,
        error: error
    };
};

export const fetchPayment = (companyId, merchantId, history) => {
    return (dispatch) => {
        axios.get(`/merchants/payment-data?companyId=${companyId}&merchantId=${merchantId}`)
            .then(response => {
                dispatch(fetchPaymentDataSuccess(response.data));
            })
            .catch(err => {
                history.push('/');
                toastr.error('Support payment methods', 'Something went wrong. Please try again.');
            });
    };
};


export const submitForm = (data,companyId, merchantId, history) => {
    return dispatch => {
        axios.post(`/merchants/payment-data?companyId=${companyId}&merchantId=${merchantId}`, data)
            .then((response) => {
                toastr.success('Support Payment Methods','Success');
                window.location.href = response.data;
            }, (err) => {
                toastr.error('Support Payment Methods', err.response.data.message);
            });
    };
};