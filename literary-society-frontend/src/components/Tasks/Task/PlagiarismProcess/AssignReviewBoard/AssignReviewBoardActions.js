import axios from '../../../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './AssignReviewBoardActionTypes';

export const fetchFormSuccess = (formFields, processInstanceId, taskId, plagiarismDetails) => {
    return {
        type: actionTypes.FETCH_FORM_SUCCESS,
        formFields: formFields,
        processInstanceId: processInstanceId,
        taskId: taskId,
        plagiarismDetails:plagiarismDetails
    };
};

export const clearState = () => {
    return {
        type: actionTypes.CLEAR_STATE,
    };
};

export const fetchForm = (piId, taskId) => {
    return dispatch => {
        axios.get(`/tasks/${taskId}/editors?piId=${piId}`)
            .then(response => {
                console.log('response', response)
                dispatch(fetchFormSuccess(response.data.formFieldsDTO.formFields, response.data.formFieldsDTO.processInstanceId,
                     response.data.formFieldsDTO.taskId, response.data.plagiarismDetails));
            })
            .catch(err => {
                toastr.error('Plagiarism Details', 'Something went wrong.Please try again.');
            });
    };
};

export const confirm = (data, taskId, history) => {
    return dispatch => {
        axios.post(`/process/${taskId}`, data)
            .then(() => {
                toastr.success('Plagiarism Details','Success');
                dispatch(clearState());
                history.push('/tasks');
            }).catch(err => {
                toastr.error('Plagiarism Details', err.response.data.message);
                history.push('/tasks');
            });
    };
};