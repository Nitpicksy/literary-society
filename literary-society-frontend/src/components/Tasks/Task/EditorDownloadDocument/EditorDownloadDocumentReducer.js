import * as actionTypes from './EditorDownloadDocumentActionTypes';

const initialState = {
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