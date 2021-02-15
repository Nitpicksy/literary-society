import axios from '../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './BookDetailsActionTypes';
import { saveAs } from 'file-saver';

export const fetchBookSuccess = (book) => {
    return {
        type: actionTypes.FETCH_BOOK_SUCCESS,
        book: book
    };
};

export const fetchBookStart = () => {
    return {
        type: actionTypes.FETCH_BOOK_START,
    };
};

export const fetchBookFail = () => {
    return {
        type: actionTypes.FETCH_BOOK_FAIL
    };
};

export const fetchBook = (id, history,needToAddToCart) => {
    return dispatch => {
        dispatch(fetchBookStart());
        axios.get(`/books/${id}`)
            .then(response => {
                dispatch(fetchBookSuccess(response.data));
                console.log(needToAddToCart)
                if(needToAddToCart){
                    addToCart(response.data.bookDTO);
                }
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

export const addToCart = (book) => {
    let shoppingCart = new Map(JSON.parse(localStorage.getItem('shoppingCart')));

    if (!shoppingCart) {
        shoppingCart = new Map();
    }

    let merchantBooks = shoppingCart.get(book.merchantName);
    if (!merchantBooks) {
        merchantBooks = [];
    }

    const found = merchantBooks.find(element => element.id === book.id);
    if (!found) {
        merchantBooks.push(book);
        shoppingCart.set(book.merchantName, merchantBooks);
        toastr.success('Shopping cart', 'Successfully added book in shopping cart');
        localStorage.setItem('shoppingCart', JSON.stringify(Array.from(shoppingCart.entries())));
    } else {
        toastr.error('Shopping cart', 'This book is already added');
    }
}

export const download = (id,title) => {
    return dispatch => {
        axios(`/books/download/${id}`, {
            method: 'GET',
            responseType: 'blob'
        }).then((response) => {
            const blob = new Blob([response.data], { type: 'application/pdf' });
            saveAs(blob, title);
        }).catch(err => {
            toastr.error('Download book', 'Something went wrong.Please try again.');
        });
    };
};