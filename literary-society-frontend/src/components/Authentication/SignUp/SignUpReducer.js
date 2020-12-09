import * as actionTypes from './SignUpActionTypes';

const initialState = {
    formFields: null,
    processInstanceId: null,
    taskId: null, 
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
                error: null
            };
        case actionTypes.FETCH_FORM_FAIL:
            return {
                ...state,
                error: action.error
            };
        case actionTypes.SIGN_UP_START:
            return {
                ...state,
                error: null
            };
        case actionTypes.SIGN_UP_SUCCESS:
            return {
                ...state,
                error: null
            };
        case actionTypes.SIGN_UP_FAIL:
            return {
                ...state,
                error: action.error
            };
        default:
            return state;
    }
};

export default reducer;