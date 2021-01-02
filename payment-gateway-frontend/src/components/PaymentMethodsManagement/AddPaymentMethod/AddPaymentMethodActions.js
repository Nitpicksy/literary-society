import axios from '../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';

export const registerPaymentMethod = (paymentMethod, history) => {
    return dispatch => {
        axios.post('/payment-methods', paymentMethod)
            .then(response => {
                if (response.data) {
                    history.push('/add-payment-method-success');
                    toastr.success('Register payment method', 'Payment method registered successfully.');
                }
            })
            .catch(err => {
                toastr.error('Register payment method', err.response.data.message);
            })
    };
};