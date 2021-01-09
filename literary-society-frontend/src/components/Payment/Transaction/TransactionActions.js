import axios from '../../../axios-endpoint';
import * as actionTypes from './TransactionActionTypes';

export const fetchTransactionStart = () => {
    return {
        type: actionTypes.FETCH_TRANSACTION_START,
    };
};


export const fetchTransactionSuccess = (transaction) => {
    if(transaction.orderedBooks.length !== 0) {
        removeBooksFromLocalStorage(transaction.orderedBooks)
    }
    return {
        type: actionTypes.FETCH_TRANSACTION_SUCCESS,
        transaction: transaction
    };
};


const removeBooksFromLocalStorage = (books) => {
    const map = new Map(JSON.parse(localStorage.getItem('shoppingCart')));
    const merchantName = books[0].merchantName;
    map.delete(merchantName);
    localStorage.setItem('shoppingCart', JSON.stringify(Array.from(map.entries())));
}

export const fetchTransaction = (transactionId) => {
    return dispatch => {
        dispatch(fetchTransactionStart());
        axios.get(`/transactions/${transactionId}`)
            .then(response => {
                dispatch(fetchTransactionSuccess(response.data));
            });
    };
};
