import axios from '../../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './EditorCheckIfBookIsPlagiarismActionTypes';
import { saveAs } from 'file-saver';

export const fetchFormSuccess = (formFields, processInstanceId, taskId,publicationRequest) => {
    return {
        type: actionTypes.FETCH_FORM_CHECK_IF_BOOK_IS_PLAGIARISM_SUCCESS,
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

export const fetchPlagiarismInfoSuccess = (plagiarismInfo) => {
    return {
        type: actionTypes.FETCH_PLAGIARISM_INFO_SUCCESS,
        plagiarismInfo: plagiarismInfo,
    };
};

export const fetchPlagiarismInfo = (piId) => {
    return dispatch => {
        axios.get(`/tasks/process-variable?piId=${piId}&name=similarPapers`)
            .then(response => {
                dispatch(fetchPlagiarismInfoSuccess(response.data));
            })
            .catch(err => {
                toastr.error('Publication request', 'Something went wrong. Please try again.');
            });
    };
};

export const clearState = () => {
    return {
        type: actionTypes.CLEAR_CHECK_IF_BOOK_IS_PLAGIARISM_STATE,
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

export const downloadPaper = (paperId, title) => {
    return dispatch => {
        axios(`/books/download-paper/${paperId}`, {
            method: 'GET',
            responseType: 'blob'
        }).then((response) => {
            const blob = new Blob([response.data], { type: 'application/pdf' });
            saveAs(blob, title);
        }).catch(err => {
            toastr.error('Download paper', 'Something went wrong. Please try again.');
        });
    };
};