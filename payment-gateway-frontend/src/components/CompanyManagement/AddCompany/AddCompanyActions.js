import axios from '../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './AddCompanyActionTypes';

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
        axios.get('/payment-methods/approved')
            .then(response => {
                dispatch(fetchPaymentMethodsSuccess(response.data));
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

export const addCompany = (company, history) => {
    return dispatch => {
        axios.post('/companies', company)
            .then(response => {
                if (response.data) {
                    history.push('/add-company-success');
                    toastr.success('Add company', 'Company added successfully.');
                }
            })
            .catch(err => {
                toastr.error('Add company', err.response.data.message);
            })
    };
};