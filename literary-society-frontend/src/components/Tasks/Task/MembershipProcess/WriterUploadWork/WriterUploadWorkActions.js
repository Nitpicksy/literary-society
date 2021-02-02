import axios from '../../../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './WriterUploadWorkActionTypes';

export const uploadSuccess = () => {
    return {
        type: actionTypes.UPLOAD_SUCCESS,
        success: true
    };
};

export const fetchDraftsSuccess = (drafts) => {
    return {
        type: actionTypes.FETCH_DRAFTS_SUCCESS,
        drafts: drafts
    };
};

export const fetchCommentsSuccess = (comments) => {
    return {
        type: actionTypes.FETCH_COMMENTS_SUCCESS,
        comments: comments
    };
};


export const fetchDrafts = () => {
    return dispatch => {
        axios.get(`/writers/drafts`)
        .then(response => {
            dispatch(fetchDraftsSuccess(response.data));
        }).catch(err => {
            console.log('error', err)
        });
    };
};

export const fetchComments = () => {
    return dispatch => {
        axios.get(`/committee/comments`)
        .then(response => {
            dispatch(fetchCommentsSuccess(response.data));
        }).catch(err => {
            console.log('error', err)
        });
    };
};


export const upload = (piId, taskId, files, history) => {
    return dispatch => {
        axios.post(`/tasks/${taskId}/writer-membership-upload?piId=${piId}`, files)
        .then(response => {
            dispatch(uploadSuccess());
            toastr.success('Document upload', 'Documents sent to committee for evaluation');
            history.push('/tasks');
        }).catch(err => {
            toastr.error('Document upload', err.response.data.message);
            history.push('/tasks');
        });
    };
};

export const fetchFormSuccess = (formFields, processInstanceId, taskId) => {
    return {
        type: actionTypes.FETCH_FORM_SUCCESS,
        processInstanceId: processInstanceId,
        taskId: taskId,
        formFields: formFields,
    };
};

export const fetchForm = (piId, taskId) => {
    return dispatch => {
        axios.get(`/tasks/${taskId}/committee?piId=${piId}`)
            .then(response => {
                dispatch(fetchFormSuccess(response.data.formFieldsDTO.formFields, response.data.formFieldsDTO.processInstanceId,
                    response.data.formFieldsDTO.taskId));
            })
            .catch(err => {
                 toastr.error('Writer upload work', 'Something went wrong. Please try again.');
            });
    };
};