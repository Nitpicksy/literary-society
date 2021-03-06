import axios from '../../axios-endpoint';
import {toastr} from 'react-redux-toastr';

export const confirmPayment = (pan, securityCode,cardHolderName,expirationDate,paymentId, history) => {
    return dispatch => {

        const data = {
            pan: pan,
            securityCode: securityCode,
            cardHolderName:cardHolderName,
            expirationDate:expirationDate
        };

        axios.post(`/payments/confirm/${paymentId}`, data)
            .then((response) => {
                window.location.href =response.data
            })
            .catch(err => {
                toastr.error('Confirm Purchase',err.response.data.message);
            })
    };
};
