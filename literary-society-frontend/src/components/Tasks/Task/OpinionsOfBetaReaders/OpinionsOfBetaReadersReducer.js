import * as actionTypes from './OpinionsOfBetaReadersActionTypes';

const initialState = {
    processInstanceId: null,
    taskId: null,
    publicationRequest: null,
    opinions: [],
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
        case actionTypes.FETCH_OPINIONS_SUCCESS:
            return {
                ...state,
                opinions: action.opinions,
                error: null
            };
        case actionTypes.CLEAR_STATE:
            return {
                ...state,
                opinions: [],
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