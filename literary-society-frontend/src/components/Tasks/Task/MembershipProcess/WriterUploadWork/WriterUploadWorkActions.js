import axios from '../../../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './WriterUploadWorkActionTypes';

export const uploadSuccess = () => {
    return {
        type: actionTypes.UPLOAD_SUCCESS,
        success: true
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