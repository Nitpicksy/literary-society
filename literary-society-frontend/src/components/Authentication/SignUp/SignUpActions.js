import axios from '../../../axios-endpoint';

import * as actionTypes from './SignUpActionTypes';


export const fetchFormSuccess = (formFields,processInstanceId,taskId ) => {
    return {
        type: actionTypes.FETCH_FORM_SUCCESS,
        formFields: formFields,
        processInstanceId:processInstanceId,
        taskId:taskId
    };
};

export const fetchFormFail = (error) => {
    return {
        type: actionTypes.FETCH_FORM_FAIL,
        error: error
    };
};

export const fetchForm = () => {
    return dispatch => {

        axios.get('/readers/start-registration')
            .then(response => {
                dispatch(fetchFormSuccess(response.data.formFields,response.data.processInstanceId,response.data.taskId));
            })
            .catch(err => {
                dispatch(fetchFormFail(err.response.data.error)); 
            })
    };
};
