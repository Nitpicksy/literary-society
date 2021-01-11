import axios from '../../../axios-endpoint';
import {toastr} from 'react-redux-toastr';

export const signUp = (name,city,country,email,username, password,repeatedPassword,history) => {
    return dispatch => {
        const data = {
            name: name,
            city: city,
            country: country,
            email:email,
            username: username,
            password: password,
            repeatedPassword: repeatedPassword
        };

        axios.post(`/merchants`, data)
            .then(() => {
                history.push('/sign-up-finished');
                toastr.success('Sign up', 'Success');
            })
            .catch(err => {
                toastr.error('Sign up',err.response.data.message);
            })
    };
};