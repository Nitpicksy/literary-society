import axios from '../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './MembershipActionTypes';

export const fetchMembershipSuccess = (membership) => {
    return {
        type: actionTypes.FETCH_MEMBERSHIP_SUCCESS,
        membership: membership
    };
};

export const fetchMembershipFail = () => {
    return {
        type: actionTypes.FETCH_MEMBERSHIP_FAIL
    };
};

export const fetchPriceListSuccess = (priceList) => {
    return {
        type: actionTypes.FETCH_PRICE_LIST_SUCCESS,
        priceList: priceList
    };
};

export const fetchPriceListFail = () => {
    return {
        type: actionTypes.FETCH_PRICE_LIST_FAIL
    };
};

export const fetchMembership = (history) => {
    return dispatch => {
        axios.get(`/memberships`)
            .then(response => {
                dispatch(fetchMembershipSuccess(response.data));
            })
            .catch(err => {
                if (err.response) {
                    dispatch(fetchMembershipFail());
                } 
            });
    };
};

export const fetchPriceList = () => {
    return dispatch => {
        axios.get(`/price-lists/latest`)
            .then(response => {
                dispatch(fetchPriceListSuccess(response.data));
            })
            .catch(err => {
                if (err.response) {
                    dispatch(fetchPriceListFail());
                    toastr.error('Price list', err.response.data.message);
                }
            });
    };
};


export const onPay = () => {
    return dispatch => {
         axios.post('/payments/pay-membership', {})
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
        }
};

export const completePayTask = (selectedTask) => {
    return dispatch => {
        if(selectedTask) { //for writer
            axios.put(`/tasks/${selectedTask.taskId}/membership?piId=${selectedTask.piId}`)
            .then(response => {
                console.log('response', response)
            })
            .catch(err => {

            });
         };
        }
};