import * as actionTypes from './PlagiarismComplaintActionTypes';

const initialState = {
    formFields: null,
    processInstanceId: null,
    taskId: null, 
    error: null
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
        default:
            return state;
    }
};

export default reducer;