import axios from '../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './MerchantBooksActionTypes';

export const fetchBooksSuccess = (books) => {
    return {
        type: actionTypes.FETCH_MERCHANT_BOOKS_SUCCESS,
        books: books
    };
};

export const fetchBooksFail = () => {
    return {
        type: actionTypes.FETCH_MERCHANT_BOOKS_FAIL
    };
};

export const fetchBooks = () => {
    return dispatch => {
        axios.get('/books/merchant')
            .then(response => {
                dispatch(fetchBooksSuccess(response.data));
            })
            .catch(err => {
                if (err.response) {
                    dispatch(fetchBooksFail());
                    toastr.error('Merchant books', err.response.data.message);
                } else {
                    toastr.error('Merchant books', 'Something went wrong. Please try again.');
                }
            });
    };
};