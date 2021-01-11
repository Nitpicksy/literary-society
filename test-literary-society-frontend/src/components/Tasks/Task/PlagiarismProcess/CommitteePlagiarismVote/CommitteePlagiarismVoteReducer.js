import * as actionTypes from './CommitteePlagiarismVoteActionTypes';

const initialState = {
    formFields: null,
    processInstanceId: null,
    taskId: null, 
    editorsComments: null,
    plagiarismDetails: null,
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
                editorsComments: action.editorsComments,
                plagiarismDetails: action.plagiarismDetails,
                error: null
            };
        default:
            return state;
    }
};

export default reducer;