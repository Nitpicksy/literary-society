import * as actionTypes from './PublicationRequestsActionTypes';

const initialState = {
    processInstanceId: null,
    taskId: null,
    publicationRequests: null,
    plagiarismTaskId: null,
    plagiarismProcessInstanceId: null,
    chosenPlagiarismBook: null,
    error: '',
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_PUBLICATION_REQUESTS_SUCCESS:
            return {
                ...state,
                publicationRequests: action.publicationRequests,
                error: null
            };
        case actionTypes.START_PROCESS_SUCCESS:
            return {
                ...state,
                processInstanceId: action.processInstanceId,
                taskId: action.taskId,
                error: null
            };
        case actionTypes.START_PROCESS_FAIL:
            return {
                ...state,
                error: action.error
            };
        case actionTypes.START_PLAGIARISM_PROCESS_SUCCESS:
            return {
                ...state,
                plagiarismProcessInstanceId: action.plagiarismProcessInstanceId,
                plagiarismTaskId: action.plagiarismTaskId,
                error: null
            };
        case actionTypes.START_PLAGIARISM_PROCESS_FAIL:
            return {
                ...state,
                error: action.error
            };
        case actionTypes.SET_CHOSEN_PLAGIARISM_BOOK:
            return {
                ...state,
                chosenPlagiarismBook: action.chosenPlagiarismBook
        };
        case actionTypes.CLEAR_PROCESS_STATE:
            return {
                ...state,
                processInstanceId: null,
                taskId: null,
            };
        default:
            return state;
    }
};

export default reducer;