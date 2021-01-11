import * as actionTypes from './EditorDownloadPlagiarismDocumentsActionTypes';

const initialState = {
    processInstanceId: null,
    taskId: null,
    plagiarismDetails: null,
    error: ''
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_FORM_SUCCESS:
            return {
                ...state,
                processInstanceId: action.processInstanceId,
                taskId: action.taskId,
                plagiarismDetails: action.plagiarismDetails,
                error: null
            };
        case actionTypes.CLEAR_STATE:
            return {
                ...state,
                processInstanceId: null,
                taskId: null,
                plagiarismDetails: null,
                error: null
            };
        default:
            return state;
    }
};

export default reducer;