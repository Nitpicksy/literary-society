import axios from '../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './BookDetailsActionTypes';

export const fetchBookSuccess = (book) => {
    return {
        type: actionTypes.FETCH_BOOK_SUCCESS,
        book: book
    };
};

export const fetchBookFail = () => {
    return {
        type: actionTypes.FETCH_BOOK_FAIL
    };
};

export const fetchBook = (id, history) => {
    return dispatch => {
        axios.get(`/books/${id}`)
            .then(response => {
                dispatch(fetchBookSuccess(response.data));
            })
            .catch(err => {
                if (err.response) {
                    dispatch(fetchBookFail());
                    toastr.error('Book details', err.response.data.message);
                } else {
                    toastr.error('Book details', 'Something went wrong. Please try again.');
                }
                history.push('/');
            });
    };
};