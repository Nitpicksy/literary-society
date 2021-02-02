import axios from '../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './CreatePublicationRequestActionTypes';

export const fetchFormSuccess = (formFields, processInstanceId, taskId) => {
    return {
        type: actionTypes.FETCH_FORM_SUCCESS,
        formFields: formFields,
        processInstanceId: processInstanceId,
        taskId: taskId
    };
};

export const fetchFormFail = (error) => {
    return {
        type: actionTypes.FETCH_FORM_FAIL,
        error: error
    };
};

export const fetchForm = (piId, taskId) => {
    return dispatch => {
        axios.get(`/books/publication-request-form?piId=${piId}&taskId=${taskId}`)
            .then(response => {
                dispatch(fetchFormSuccess(response.data.formFields, response.data.processInstanceId, response.data.taskId));
            })
            .catch(err => {
                if (err.response) {
                    dispatch(fetchFormFail(err.response.data.message));
                    toastr.error('Create Publication Request', err.response.data.message);
                } else {
                    toastr.error('Create Publication Request', 'Something went wrong');
                }
            });
    };
};

export const createRequest = (enteredData, taskId, history) => {
    axios.post('/process/' + taskId, enteredData)
        .then(() => {
            toastr.success('Create Publication Request', 'Successfully created.');
            history.push('/publication-requests');
        }, (err) => {
            toastr.error('Create Publication Request', err.response.data.message);
        })
};