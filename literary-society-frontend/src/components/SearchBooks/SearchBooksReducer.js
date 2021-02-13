import * as actionTypes from './SearchBooksActionTypes';

const initialState = {
    books: null
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.SEARCH_BOOKS_SUCCESS:
            return {
                ...state,
                books: action.books,
            };
        case actionTypes.SEARCH_BOOKS_FAIL:
            return {
                ...state,
                books: null,
            };
        case actionTypes.SEARCH_BOOKS_START:
            return {
                ...state,
                books: null,
            };
        default:
            return state;
    }
};

export default reducer;