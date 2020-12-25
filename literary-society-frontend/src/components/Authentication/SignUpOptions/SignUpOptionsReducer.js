import * as actionTypes from './SignUpOptionsActionTypes';

const initialState = {
    processInstanceId: null,
    taskId: null,
    error: '',
    signUpType: 'readers' //defaults to readers
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.SIGN_UP_TYPE:
            return {
                ...state,
                signUpType: action.signUpType
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