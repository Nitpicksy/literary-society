import axios from '../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './TransactionListActionTypes';

export const fetchTransactionsSuccess = (transactions) => {
    return {
        type: actionTypes.FETCH_TRANSACTIONS_SUCCESS,
        transactions: transactions
    };
};

export const fetchTransactionsFail = () => {
    return {
        type: actionTypes.FETCH_TRANSACTIONS_FAIL
    };
};

export const fetchTransactions = () => {
    return dispatch => {
        axios.get('/transactions')
            .then(response => {
                dispatch(fetchTransactionsSuccess(response.data));                
            })
            .catch(err => {
                dispatch(fetchTransactionsFail());
                toastr.error('Transactions', 'Something went wrong. Please try again.');
            });
    };
};