import * as actionTypes from './WriterUploadWorkActionTypes';

const initialState = {
    success: false,
    comments: null,
    drafts: null
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.UPLOAD_SUCCESS:
            return {
                ...state,
               success: action.success
            };
        case actionTypes.FETCH_DRAFTS_SUCCESS:
            return {
                ...state,
                drafts: action.drafts
            };
        case actionTypes.FETCH_COMMENTS_SUCCESS:
            return {
                ...state,
                comments: action.comments
            };
        default:
            return state;
    }
};

export default reducer;