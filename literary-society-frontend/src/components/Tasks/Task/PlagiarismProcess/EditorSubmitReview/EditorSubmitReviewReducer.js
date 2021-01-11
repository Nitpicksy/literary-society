import * as actionTypes from './AssignReviewBoardActionTypes';

const initialState = {
    formFields: null,
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
                formFields: action.formFields,
                processInstanceId: action.processInstanceId,
                taskId: action.taskId,
                plagiarismDetails: action.plagiarismDetails,
                error: null
            };
        case actionTypes.CLEAR_STATE:
            return {
                ...state,
                formFields: null,
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