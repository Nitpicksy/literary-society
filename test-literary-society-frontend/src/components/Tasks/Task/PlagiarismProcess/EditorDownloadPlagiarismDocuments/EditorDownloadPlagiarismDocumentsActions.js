import axios from '../../../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './EditorDownloadPlagiarismDocumentsActionTypes';
import { saveAs } from 'file-saver';

export const fetchFormSuccess = (processInstanceId, taskId, plagiarismDetails) => {
    return {
        type: actionTypes.FETCH_FORM_SUCCESS,
        processInstanceId: processInstanceId,
        taskId: taskId,
        plagiarismDetails: plagiarismDetails
    };
};

export const fetchForm = (piId, taskId) => {
    return dispatch => {
        axios.get(`/tasks/${taskId}/editors?piId=${piId}`)
            .then(response => {
                dispatch(fetchFormSuccess(response.data.formFieldsDTO.processInstanceId,
                    response.data.formFieldsDTO.taskId, response.data.plagiarismDetails));
            })
            .catch(err => {
                toastr.error('Plagiarism report books', 'Something went wrong.Please try again.');
            });
    };
};

export const clearState = () => {
    return {
        type: actionTypes.CLEAR_STATE,
    };
};

export const download = (piId, taskId, writerTitle, type, history, downloaded) => {
    return dispatch => {
        axios(`/tasks/${taskId}/download-plagiarism?piId=${piId}&type=${type}`, {
            method: 'PUT',
            responseType: 'blob'
        }).then((response) => {
            console.log('do', downloaded)
            let blob = new Blob([response.data], { type: 'application/pdf' });
            saveAs(blob, writerTitle);

            if(downloaded) {
                history.push('/tasks');
                // dispatch(clearState());
            }
        }).catch(err => {
            toastr.error('Publication Request', 'Something went wrong.Please try again.');
            history.push('/tasks');
        });
    };
};
