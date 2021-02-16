import * as actionTypes from './EditorChooseBetaReadersActionTypes';
import { extractSelectItems } from '../../../../utility/extractSelectItems';

const initialState = {
    formFields: null,
    processInstanceId: null,
    taskId: null,
    publicationRequest: null,
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
                publicationRequest: action.publicationRequest,
                error: null
            };
        case actionTypes.FILTER_BETA_READERS_SUCCESS:
            let updatedFormFields = [...state.formFields];
            updatedFormFields[0].type.values = extractSelectItems(action.betaReaders);
            return {
                ...state,
                formFields: updatedFormFields,
            };
        case actionTypes.CLEAR_STATE:
            return {
                ...state,
                formFields: null,
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