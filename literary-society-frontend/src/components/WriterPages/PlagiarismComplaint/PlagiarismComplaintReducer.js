import * as actionTypes from './PlagiarismComplaintActionTypes';

const initialState = {
    formFields: null,
    processInstanceId: null,
    taskId: null, 
    error: null,
    valid: null,
    array: null
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_FORM_SUCCESS:
            return {
                ...state,
                formFields: action.formFields,
                processInstanceId: action.processInstanceId,
                taskId: action.taskId
            };
        case actionTypes.FETCH_FORM_FAIL:
            return {
                ...state,
                error: action.error
            };
        case actionTypes.VALIDATE_SUCCESS:
            return {
                ...state,
                valid: true
            };
        case actionTypes.VALIDATE_FAIL:
            return {
                ...state,
                valid: false
            };
        case actionTypes.CLEAR_VALIDATION:
            return {
                ...state,
                valid: null
            };
        default:
            return state;
    }
};

export default reducer;