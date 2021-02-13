import axios from '../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './SearchBooksActionTypes';

export const searchBooksSuccess = (books) => {
    return {
        type: actionTypes.SEARCH_BOOKS_SUCCESS,
        books: books
    };
};

export const searchBooksFail = () => {
    return {
        type: actionTypes.SEARCH_BOOKS_FAIL
    };
};

export const searchBooksStart = () => {
    return {
        type: actionTypes.SEARCH_BOOKS_START
    };
};

export const searchAll = (page,searchValue) => {
    return dispatch => {
        dispatch(searchBooksStart());
        axios.post(`/books/search?page=${page}&&size=5&&searchValue=${searchValue}`,[] )
            .then(response => {
                dispatch(searchBooksSuccess(response.data.content));
            })
            .catch(err => {
                if (err.response) {
                    dispatch(searchBooksFail());
                    toastr.error('Search books', 'Something went wrong. Please try again.');
                } 
            });
    };
};

export const combineSearchParams = (page,searchParams) => {
    return dispatch => {
        dispatch(searchBooksStart());
        axios.post(`/books/search?page=${page}&&size=5`,searchParams )
            .then(response => {
                dispatch(searchBooksSuccess(response.data.content));
            })
            .catch(err => {
                if (err.response) {
                    dispatch(searchBooksFail());
                    toastr.error('Search books', 'Something went wrong. Please try again.');
                } 
            });
    };
};