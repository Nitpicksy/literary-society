import * as actionTypes from './PublicationRequestsActionTypes';

const initialState = {
    processInstanceId: null,
    taskId: null,
    error: '',
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
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