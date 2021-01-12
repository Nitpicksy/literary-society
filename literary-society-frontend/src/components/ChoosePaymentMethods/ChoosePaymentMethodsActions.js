import axios from '../../axios-endpoint';
import { toastr } from 'react-redux-toastr';

export const choosePaymentMethods = () => {
    return dispatch => {
        axios.get('/payments/choose-payment-methods')
            .then(response => {
                if (response.data) {
                    window.location.href = response.data;
                }
            })
            .catch(() => {
                toastr.error('Choose Payment Methods', 'Something went wrong. Please try again.');
            });
    }
};