import * as actionTypes from './EditorChooseBetaReadersActionTypes';

const initialState = {
    formFields: null,
    processInstanceId: null,
    taskId: null, 
    publicationRequest:null,
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
                publicationRequest:action.publicationRequest,
                error: null
            };
        default:
            return state;
    }
};

export default reducer;