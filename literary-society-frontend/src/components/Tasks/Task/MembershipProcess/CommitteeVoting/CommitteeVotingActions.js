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