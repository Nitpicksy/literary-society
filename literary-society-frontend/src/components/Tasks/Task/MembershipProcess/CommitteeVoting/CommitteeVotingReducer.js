import * as actionTypes from './CommitteeVotingActionTypes';

const initialState = {
    formFields: null,
    processInstanceId: null,
    taskId: null, 
    writerDocuments:null,
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
                writerDocuments: action.writerDocuments,
                error: null
            };
        default:
            return state;
    }
};

export default reducer;