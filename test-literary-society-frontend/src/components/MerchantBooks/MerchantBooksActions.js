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

export const addBook = (data, history) => {
    return dispatch => {
        axios.post('/books', data)
            .then(response => {
                console.log("push")
                history.push('/merchant-books');
                toastr.success('Add book', 'Book added successfully.');
            })
            .catch(err => {
                toastr.error('Add book', err.response.data.message);
            })
    };
};


export const fetchGenresSuccess = (genres) => {
    let array = [];
    for (let i in genres) {
        array.push({ value: genres[i].id, displayValue: genres[i].name });
    }
    return {
        type: actionTypes.FETCH_GENRES_SUCCESS,
        genres: array
    };
};

export const fetchGenresFail = () => {
    return {
        type: actionTypes.FETCH_GENRES_FAIL
    };
};

export const fetchGenres = () => {
    return dispatch => {
        axios.get('/genres')
            .then(response => {
                dispatch(fetchGenresSuccess(response.data));
            })
            .catch(err => {
                if (err.response) {
                    dispatch(fetchGenresFail());
                    toastr.error('Fetch genres', err.response.data.message);
                } else {
                    toastr.error('Fetch genres', 'Something went wrong. Please try again.');
                }
            });
    };
};