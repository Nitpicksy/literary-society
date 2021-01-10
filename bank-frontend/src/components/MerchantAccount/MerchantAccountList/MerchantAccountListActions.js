import axios from '../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './MerchantAccountListActionTypes';

export const fetchMerchantsSuccess = (merchants) => {
    return {
        type: actionTypes.FETCH_MERCHANTS_SUCCESS,
        merchants: merchants
    };
};

export const fetchMerchantsFail = () => {
    return {
        type: actionTypes.FETCH_MERCHANTS_FAIL
    };
};

export const fetchMerchants = () => {
    return dispatch => {
        axios.get('/merchants')
            .then(response => {
                dispatch(fetchMerchantsSuccess(response.data));                
            })
            .catch(err => {
                dispatch(fetchMerchantsFail());
                toastr.error('Merchants', 'Something went wrong. Please try again.');
            });
    };
};
