import axios from '../../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './WriterUploadDocumentActionTypes';

export const fetchFormSuccess = (formFields, processInstanceId, taskId, publicationRequest) => {
    return {
        type: actionTypes.FETCH_FORM_SUCCESS,
        formFields: formFields,
        processInstanceId: processInstanceId,
        taskId: taskId,
        publicationRequest: publicationRequest,
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
                toastr.error('Upload manuscript', 'Something went wrong. Please try again.');
            });
    };
};

export const clearState = () => {
    return {
        type: actionTypes.CLEAR_STATE,
    };
};


export const upload = (piId, taskId, pdfFormData, history) => {
    return dispatch => {
        axios.post(`/tasks/${taskId}/complete-and-upload?piId=${piId}`, pdfFormData)
        .then(response => {
            toastr.success('Upload manuscript', 'Manuscript uploaded successfully.');
            dispatch(clearState());
            history.push('/tasks');
        }).catch(err => {
            toastr.error('Upload manuscript', err.response.data.message);
            history.push('/tasks');
        });
    };
};
