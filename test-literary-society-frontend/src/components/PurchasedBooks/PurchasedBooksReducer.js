import * as actionTypes from './PurchasedBooksActionTypes';

const initialState = {
    books: null
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_PURCHASED_BOOKS_SUCCESS:
            return {
                ...state,
                books: action.books,
            };
        case actionTypes.FETCH_PURCHASED_BOOKS_FAIL:
            return {
                ...state,
                books: null,
            };
        default:
            return state;
    }
};

export default reducer;