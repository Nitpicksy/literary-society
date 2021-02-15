import * as actionTypes from './EditorChooseBetaReadersActionTypes';

const initialState = {
    formFields: null,
    processInstanceId: null,
    taskId: null,
    publicationRequest: null,
    error: '',
    betaReaders: null
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
        case actionTypes.CLEAR_STATE:
            return {
                ...state,
                formFields: null,
                processInstanceId: null,
                taskId: null,
                publicationRequest: null,
                error: null
            };
        case actionTypes.FILTER_BETA_READERS_SUCCESS:
            return {
                ...state,
                betaReaders: action.betaReaders, 
                formFields: updateFormFields(action.betaReaders, action.formFields)
            };
        default:
            return state;
    }
};


const updateFormFields = (betaReaders, formFields) => {
    let updatedFormFields = formFields;
    if (updatedFormFields) {
        updatedFormFields[0].type.values = extractEnum(betaReaders);
    }
    return updatedFormFields;
}

const extractEnum = (betaReaders) => {
    let map = new Map();
    for (let i in betaReaders) {
        map.set(betaReaders[i].key, betaReaders[i].value);
    }

    return Object.fromEntries(map);
}

export default reducer;