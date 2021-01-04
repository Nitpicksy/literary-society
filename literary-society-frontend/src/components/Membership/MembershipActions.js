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
                    // toastr.error('Membership', err.response.data.message);
                } else {
                    // toastr.error('Membership', 'Something went wrong. Please try again.');
                }
                // history.push('/');
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
                    toastr.error('PriceList', err.response.data.message);
                } else {
                    // toastr.error('Membership', 'Something went wrong. Please try again.');
                }
                // history.push('/');
            });
    };
};


export const onPay = (selectedTask) => {
    return dispatch => {
        console.log('selt', selectedTask)
        if(selectedTask) {
            axios.put(`/tasks/${selectedTask.taskId}/membership?piId=${selectedTask.piId}`)
            .then(response => {
                toastr.success("aw","yesah")
                console.log('RES', response)
            })
            .catch(err => {
            });
    };
        }

};