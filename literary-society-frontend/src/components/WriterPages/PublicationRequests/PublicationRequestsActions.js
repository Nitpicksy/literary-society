import axios from '../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './PublicationRequestsActionTypes';

export const fetchPublicationRequestsSuccess = (publicationRequests) => {
    return {
        type: actionTypes.FETCH_PUBLICATION_REQUESTS_SUCCESS,
        publicationRequests: publicationRequests
    };
};

export const fetchPublicationRequestsFail = () => {
    return {
        type: actionTypes.FETCH_PUBLICATION_REQUESTS_FAIL
    };
};

export const fetchPublicationRequests = () => {
    return dispatch => {
        axios.get('/books/publication-requests')
            .then(response => {
                dispatch(fetchPublicationRequestsSuccess(response.data));                
            })
            .catch(err => {
                dispatch(fetchPublicationRequestsFail());
                toastr.error('Publication Requests', 'Something went wrong. Please try again.');
            });
    };
};

export const startProcessSuccess = (processInstanceId, taskId) => {
    return {
        type: actionTypes.START_PROCESS_SUCCESS,
        processInstanceId: processInstanceId,
        taskId: taskId
    };
};

export const startProcessFail = (error) => {
    return {
        type: actionTypes.START_PROCESS_FAIL,
        error: error
    };
};

export const startProcess = () => {
    return (dispatch) => {
        axios.get('/books/start-publishing')
            .then(response => {
                dispatch(startProcessSuccess(response.data.processInstanceId, response.data.taskId));
            })
            .catch(err => {
                if (err.response) {
                    dispatch(startProcessFail(err.response.data.message));
                    toastr.error('Create Publication Request', err.response.data.message);
                } else {
                    toastr.error('Create Publication Request', 'Something went wrong');
                }
            });
    };
};

export const clearProcessState = () => {
    return {
        type: actionTypes.CLEAR_PROCESS_STATE
    };
};
