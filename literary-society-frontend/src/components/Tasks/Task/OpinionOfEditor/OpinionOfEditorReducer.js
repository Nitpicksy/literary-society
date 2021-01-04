import * as actionTypes from './OpinionOfEditorActionTypes';

const initialState = {
    processInstanceId: null,
    taskId: null,
    publicationRequest: null,
    opinion: null,
    error: ''
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_FORM_SUCCESS:
            return {
                ...state,
                processInstanceId: action.processInstanceId,
                taskId: action.taskId,
                publicationRequest: action.publicationRequest,
                error: null
            };
        case actionTypes.FETCH_OPINION_SUCCESS:
            return {
                ...state,
                opinion: action.opinion,
                error: null
            };
        case actionTypes.CLEAR_STATE:
            return {
                ...state,
                opinion: null,
                processInstanceId: null,
                taskId: null,
                publicationRequest: null,
                error: null
            };
        default:
            return state;
    }
};

export default reducer;