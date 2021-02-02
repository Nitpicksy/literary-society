import axios from '../../../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './CommitteePlagiarismVoteActionTypes';

export const fetchFormSuccess = (formFields, processInstanceId, taskId, plagiarismDetails, editorsComments) => {
    return {
        type: actionTypes.FETCH_FORM_SUCCESS,
        processInstanceId: processInstanceId,
        taskId: taskId,
        formFields: formFields,
        editorsComments: editorsComments,
        plagiarismDetails: plagiarismDetails
    };
};

export const fetchForm = (piId, taskId) => {
    return dispatch => {
        axios.get(`/tasks/${taskId}/editors?piId=${piId}`)
            .then(response => {
                dispatch(fetchFormSuccess(response.data.formFieldsDTO.formFields, response.data.formFieldsDTO.processInstanceId,
                    response.data.formFieldsDTO.taskId, response.data.plagiarismDetails, response.data.editorsComments));
            })
            .catch(err => {
                toastr.error('Committee voting', 'Something went wrong.Please try again.');
            });
    };
};

export const confirm = (data, taskId, history) => {
    return dispatch => {
        axios.post(`/process/${taskId}`, data)
            .then(() => {
                toastr.success('Voting','Success');
                history.push('/tasks');
            }).catch(err => {
                toastr.error('Voting', err.response.data.message);
                history.push('/tasks');
            });
    };
};