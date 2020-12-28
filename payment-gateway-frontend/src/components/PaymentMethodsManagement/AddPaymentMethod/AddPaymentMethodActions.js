import axios from '../../../axios-endpoint';
import {toastr} from 'react-redux-toastr';

export const registerPaymentMethod = (mainData, certificateData, paymentData, history) => {
    return dispatch => {
        console.log(certificateData)
        const data = {
            mainData: mainData,
            certificate: certificateData, 
            paymentData:paymentData
        };
        axios.post('/payment-methods', data)
            .then(response => {
                if (response.data) {
                    history.push('/add-payment-method-success');
                    toastr.success('Register payment method', 'Success');
                }
            })
            .catch(err => {
                toastr.error('Register payment method',err.response.data.message);
            })
    };
};