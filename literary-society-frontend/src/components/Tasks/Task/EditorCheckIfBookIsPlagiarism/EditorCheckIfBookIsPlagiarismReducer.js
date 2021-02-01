import * as actionTypes from './EditorCheckIfBookIsPlagiarismActionTypes';

const initialState = {
    formFields: null,
    processInstanceId: null,
    taskId: null,
    publicationRequest: null,
    plagiarismInfo: null,
    error: ''
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_FORM_CHECK_IF_BOOK_IS_PLAGIARISM_SUCCESS:
            return {
                ...state,
                formFields: action.formFields,
                processInstanceId: action.processInstanceId,
                taskId: action.taskId,
                publicationRequest: action.publicationRequest,
                error: null
            };
        case actionTypes.CLEAR_CHECK_IF_BOOK_IS_PLAGIARISM_STATE:
            return {
                ...state,
                formFields: null,
                processInstanceId: null,
                taskId: null,
                publicationRequest: null,
                error: null,
                plagiarismInfo: null
            };
        case actionTypes.FETCH_PLAGIARISM_INFO_SUCCESS:
            return {
                ...state,
                plagiarismInfo: action.plagiarismInfo
            };
        default:
            return state;
    }
};

export default reducer;