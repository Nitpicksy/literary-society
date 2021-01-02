import axios from '../../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './OpinionsOfBetaReadersActionTypes';

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
                toastr.error('Upload edited manuscript', 'Something went wrong. Please try again.');
            });
    };
};

export const fetchOpinionsSuccess = (opinions) => {
    return {
        type: actionTypes.FETCH_OPINIONS_SUCCESS,
        opinions: opinions
    };
};

export const fetchOpinions = (bookId) => {
    return dispatch => {
        axios.get(`/books/${bookId}/opinions-of-beta-readers`)
            .then(response => {
                dispatch(fetchOpinionsSuccess(response.data));
            })
            .catch(err => {
                toastr.error('Upload edited manuscript', 'Something went wrong. Please try again.');
            });
    };
};

export const upload = (piId, taskId, pdfFormData, history) => {
    return dispatch => {
        axios.post(`/tasks/${taskId}/complete-and-upload?piId=${piId}`, pdfFormData)
        .then(response => {
            toastr.success('Upload edited manuscript', 'Edited manuscript uploaded successfully.');
            history.push('/tasks');
        }).catch(err => {
            toastr.error('Upload edited manuscript', err.response.data.message);
            history.push('/tasks');
        });
    };
};
