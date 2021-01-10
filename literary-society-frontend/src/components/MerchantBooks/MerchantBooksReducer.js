import * as actionTypes from './MerchantBooksActionTypes';

const initialState = {
    books: null, 
    genres: null
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_MERCHANT_BOOKS_SUCCESS:
            return {
                ...state,
                books: action.books,
            };
        case actionTypes.FETCH_MERCHANT_BOOKS_FAIL:
            return {
                ...state,
                books: null,
            };
        case actionTypes.FETCH_GENRES_SUCCESS:
            return {
                ...state,
                genres: action.genres,
            };
        case actionTypes.FETCH_GENRES_FAIL:
            return {
                ...state,
                genres: null,
            };
        default:
            return state;
    }
};

export default reducer;