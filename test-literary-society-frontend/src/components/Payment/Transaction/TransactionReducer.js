import * as actionTypes from './TransactionActionTypes';

const initialState = {
    transaction: null,
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_TRANSACTION_START:
            return {
                ...state,
                transaction: null
            };
        case actionTypes.FETCH_TRANSACTION_SUCCESS:
            return {
                ...state,
                transaction: action.transaction,
            };
        default:
            return state;
    }
};

export default reducer;