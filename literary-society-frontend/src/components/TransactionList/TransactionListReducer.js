import * as actionTypes from './TransactionListActionTypes';

const initialState = {
    transactions: null,
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_TRANSACTIONS_SUCCESS:
            return {
                ...state,
                transactions: action.transactions,
            };
        case actionTypes.FETCH_TRANSACTIONS_FAIL:
            return {
                ...state,
                transactions: null,
            };
        default:
            return state;
    }
};

export default reducer;