import axios from '../../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './EditorDownloadDocumentActionTypes';
import { saveAs } from 'file-saver';

export const fetchFormSuccess = (processInstanceId, taskId, publicationRequest) => {
    return {
        type: actionTypes.FETCH_FORM_SUCCESS,
        processInstanceId: processInstanceId,
        taskId: taskId,
        publicationRequest: publicationRequest
    };
};

export const fetchForm = (piId, taskId) => {
    return dispatch => {
        axios.get(`/tasks/${taskId}?piId=${piId}`)
            .then(response => {
                dispatch(fetchFormSuccess(response.data.formFieldsDTO.processInstanceId,
                    response.data.formFieldsDTO.taskId, response.data.publicationRequestDTO));
            })
            .catch(err => {
                toastr.error('Publication request', 'Something went wrong.Please try again.');
            });
    };
};

export const download = (piId, taskId, title, history) => {
    return dispatch => {
        axios(`/tasks/${taskId}/complete-and-download?piId=${piId}`, {
            method: 'PUT',
            responseType: 'blob'
        }).then((response) => {
            const blob = new Blob([response.data], { type: 'application/pdf' });
            saveAs(blob, title);
            history.push('/tasks');
        }).catch(err => {
            toastr.error('Publication Request', 'Something went wrong.Please try again.');
            history.push('/tasks');
        });
    };
};
