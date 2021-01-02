import axios from '../../axios-endpoint';
import { toastr } from 'react-redux-toastr';

export const insertPaymentData = () => {
    return dispatch => {
        axios.get('/merchants/payment-data')
            .then(response => {
                if (response.data) {
                    window.location.href = response.data;
                }
            })
            .catch(() => {
                toastr.error('Insert Payment Data', 'Something went wrong. Please try again.');
            });
    }
};