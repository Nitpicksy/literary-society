import axios from '../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './PurchasedBooksActionTypes';

export const fetchBooksSuccess = (books) => {
    return {
        type: actionTypes.FETCH_PURCHASED_BOOKS_SUCCESS,
        books: books
    };
};

export const fetchBooksFail = () => {
    return {
        type: actionTypes.FETCH_PURCHASED_BOOKS_FAIL
    };
};

export const fetchBooks = () => {
    return dispatch => {
        axios.get('/books/purchased')
            .then(response => {
                dispatch(fetchBooksSuccess(response.data));
            })
            .catch(err => {
                if (err.response) {
                    dispatch(fetchBooksFail());
                    toastr.error('Purchased books', err.response.data.message);
                } else {
                    toastr.error('Purchased books', 'Something went wrong. Please try again.');
                }
            });
    };
};