import axios from '../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './ClientAccountListActionTypes';

export const fetchClientsSuccess = (clients) => {
    return {
        type: actionTypes.FETCH_CLIENTS_SUCCESS,
        clients: clients
    };
};

export const fetchClientsFail = () => {
    return {
        type: actionTypes.FETCH_CLIENTS_FAIL
    };
};

export const fetchClients = () => {
    return dispatch => {
        axios.get('/accounts')
            .then(response => {
                dispatch(fetchClientsSuccess(response.data));                
            })
            .catch(err => {
                dispatch(fetchClientsFail());
                toastr.error('Clients', 'Something went wrong. Please try again.');
            });
    };
};
