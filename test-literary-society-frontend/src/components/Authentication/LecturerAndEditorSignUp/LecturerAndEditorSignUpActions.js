import axios from '../../../axios-endpoint';
import {toastr} from 'react-redux-toastr';

export const signUp = (signUpType,firstName,lastName,city,country,email,username, password,repeatedPassword,history) => {
    return dispatch => {
        console.log(signUpType)
        const data = {
            firstName: firstName,
            lastName: lastName,
            city: city,
            country: country,
            email:email,
            username: username,
            password: password,
            repeatedPassword: repeatedPassword
        };

        axios.post(`/users?type=${signUpType}`, data)
            .then(() => {
                history.push('/sign-up-finished');
                toastr.success('Sign up', 'Success');
            })
            .catch(err => {
                toastr.error('Sign up',err.response.data.message);
            })
    };
};