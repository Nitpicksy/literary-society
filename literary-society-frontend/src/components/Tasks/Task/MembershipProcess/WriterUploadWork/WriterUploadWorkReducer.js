import * as actionTypes from './WriterUploadWorkActionTypes';

const initialState = {
    success: false
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.UPLOAD_SUCCESS:
            return {
                ...state,
               success: action.success
            };
        default:
            return state;
    }
};

export default reducer;