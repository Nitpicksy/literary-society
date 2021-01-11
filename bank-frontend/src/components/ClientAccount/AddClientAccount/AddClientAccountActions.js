import axios from '../../../axios-endpoint';
import {toastr} from 'react-redux-toastr';

export const addClient = (firstName,lastName,city,country,email,balance, history) => {
    return dispatch => {
        const data = {
            firstName: firstName,
            lastName: lastName,
            city: city,
            country: country,
            email: email,
            balance: balance
        };

        axios.post('/accounts', data)
            .then(response => {
                history.push('/clients')
                toastr.success('Create account', 'Success');
            })
            .catch(err => {
                toastr.error('Create account',err.response.data.message);
            })
    };
};