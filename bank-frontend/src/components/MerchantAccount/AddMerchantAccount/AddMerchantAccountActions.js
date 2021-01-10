import axios from '../../../axios-endpoint';
import {toastr} from 'react-redux-toastr';

export const addMerchant = (name, merchantPassword,city,country,email,balance, history) => {
    return dispatch => {
        const data = {
            name: name,
            merchantPassword: merchantPassword,
            city: city,
            country: country,
            email: email,
            balance: balance
        };

        axios.post('/merchants', data)
            .then(response => {
                history.push('/merchants')
                toastr.success('Create merchant', 'Success');
            })
            .catch(err => {
                toastr.error('Create merchant',err.response.data.message);
            })
    };
};