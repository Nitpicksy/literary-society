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
            toastr.success('Document upload', 'Documents uploaded successfully.');
            history.push('/tasks');
        }).catch(err => {
            toastr.error('Document upload', err.response.data.message);
            history.push('/tasks');
        });
    };
};