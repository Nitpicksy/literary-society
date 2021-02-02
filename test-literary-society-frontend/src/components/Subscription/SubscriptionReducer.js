import * as actionTypes from './SubscriptionActionTypes';

const initialState = {
    subscriptionPlan: null
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_SUBSCRIPTION_PLAN_SUCCESS:
            return {
                ...state,
                subscriptionPlan: action.subscriptionPlan,
            };
        default:
            return state;
    }
};

export default reducer;