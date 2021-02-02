import axios from '../../../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './CommitteeVotingActionTypes';

export const fetchFormSuccess = (formFields, processInstanceId, taskId, writerDocuments) => {
    return {
        type: actionTypes.FETCH_FORM_SUCCESS,
        processInstanceId: processInstanceId,
        taskId: taskId,
        formFields: formFields,
        writerDocuments: writerDocuments
    };
};

export const fetchForm = (piId, taskId) => {
    return dispatch => {
        axios.get(`/tasks/${taskId}/committee?piId=${piId}`)
            .then(response => {
                dispatch(fetchFormSuccess(response.data.formFieldsDTO.formFields, response.data.formFieldsDTO.processInstanceId,
                    response.data.formFieldsDTO.taskId, response.data.writerDocumentDTO));
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


export const vote = (data) => {
    return dispatch => {
        axios.post(`/committee/vote`, data)
            .then(() => {
                toastr.success('Voting','Success');
            }).catch(err => {
                toastr.error('Voting', err.response.data.message);
            });
    };
};