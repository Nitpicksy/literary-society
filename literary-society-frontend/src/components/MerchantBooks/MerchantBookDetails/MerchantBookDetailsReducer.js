import * as actionTypes from './MerchantBookDetailsActionTypes';

const initialState = {
    book: null
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_MERCHANT_BOOK_SUCCESS:
            return {
                ...state,
                book: action.book,
            };
        case actionTypes.FETCH_MERCHAN_BOOK_FAIL:
            return {
                ...state,
                book: null,
            };
        default:
            return state;
    }
};

export default reducer;