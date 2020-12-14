import axios from '../../axios-endpoint';
import { toastr } from 'react-redux-toastr';

export const proceedToPayment = (books) => {
    axios.post('/payments/pay', books)
            .then(response => {
                window.location.href = response.data;
            })
            .catch(err => {
                if (err.response) {
                    if (err.response.status !== 401) {
                        toastr.error('Proceed to Payment', err.response.data.message);
                    }
                }
            });
};