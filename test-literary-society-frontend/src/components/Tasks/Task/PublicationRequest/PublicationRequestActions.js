import axios from '../../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './PublicationRequestActionTypes';

export const fetchFormSuccess = (formFields, processInstanceId, taskId,publicationRequest) => {
    return {
        type: actionTypes.FETCH_FORM_SUCCESS,
        formFields: formFields,
        processInstanceId: processInstanceId,
        taskId: taskId,
        publicationRequest:publicationRequest
    };
};

export const fetchForm = (piId, taskId) => {
    return dispatch => {
        axios.get(`/tasks/${taskId}?piId=${piId}`)
            .then(response => {
                dispatch(fetchFormSuccess(response.data.formFieldsDTO.formFields, response.data.formFieldsDTO.processInstanceId,
                     response.data.formFieldsDTO.taskId, response.data.publicationRequestDTO));
            })
            .catch(err => {
                toastr.error('Publication request', 'Something went wrong.Please try again.');
            });
    };
};

export const clearState = () => {
    return {
        type: actionTypes.CLEAR_STATE,
    };
};

export const confirm = (data, taskId, history) => {
    return dispatch => {
        axios.post(`/process/${taskId}`, data)
            .then(() => {
                toastr.success('Publication Request','Success');
                dispatch(clearState());
                history.push('/tasks');
            }).catch(err => {
                toastr.error('Publication Request', err.response.data.message);
                history.push('/tasks');
            });
    };
};