import axios from '../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './SubscriptionActionTypes';

export const fetchSubscriptionPlanSuccess = (subscriptionPlan) => {
    return {
        type: actionTypes.FETCH_SUBSCRIPTION_PLAN_SUCCESS,
        subscriptionPlan: subscriptionPlan
    };
};

export const fetchSubscriptionPlan = (forUser) => {
    return dispatch => {
        axios.get(`/subscriptions/plan?for=${forUser}`)
            .then(response => {
                dispatch(fetchSubscriptionPlanSuccess(response.data));
            })
            .catch(err => {
                toastr.error('Subscription Plans', 'Something went wrong. Please try again.');
            });
    };
};

export const subscribe = (planId) => {
    axios.post(`/subscriptions/subscribe?planId=${planId}`)
        .then(response => {
            window.location.href = response.data;
        })
        .catch(err => {
            toastr.error('Subscribe', err.response.data.message);
        });
};