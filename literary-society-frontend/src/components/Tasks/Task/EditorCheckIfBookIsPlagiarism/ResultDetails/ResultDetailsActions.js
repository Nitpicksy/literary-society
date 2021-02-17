import axios from '../../../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './ResultDetailsActionTypes';

export const fetchResultStart = () => {
    return {
        type: actionTypes.FETCH_RESULT_START
    };
};

export const fetchResultSuccess = (plagiarismInfo) => {
    return {
        type: actionTypes.FETCH_RESULT_SUCCESS,
        plagiarismInfo: plagiarismInfo,
    };
};

export const fetchResultError = () => {
    return {
        type: actionTypes.FETCH_RESULT_ERROR,
    };
};

export const fetchPlagiarismInfo = (piId) => {
    return dispatch => {
        dispatch(fetchResultStart());
        axios.get(`/books/plagiarism-result?piId=${piId}`)
            .then(response => {
                dispatch(fetchResultSuccess(response.data));
            })
            .catch(err => {
                dispatch(fetchResultError());
                toastr.error('Plagiarism info', 'Something went wrong. Please try again.');
            });
    };
};
