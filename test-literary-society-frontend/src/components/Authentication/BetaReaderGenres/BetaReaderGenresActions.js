import axios from '../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './BetaReaderGenresActionTypes';

export const fetchFormSuccess = (formFields, processInstanceId, taskId) => {
    return {
        type: actionTypes.FETCH_FORM_SUCCESS,
        formFields: formFields,
        processInstanceId: processInstanceId,
        taskId: taskId
    };
};

export const fetchFormFail = (error) => {
    return {
        type: actionTypes.FETCH_FORM_FAIL,
        error: error
    };
};

export const fetchForm = (piId) => {
    return dispatch => {
        axios.get(`/readers/beta/choose-genres?piId=${piId}`)
            .then(response => {
                dispatch(fetchFormSuccess(response.data.formFields, response.data.processInstanceId, response.data.taskId));
            })
            .catch(err => {
                if (err.response) {
                    dispatch(fetchFormFail(err.response.data.message));
                    toastr.error('Choose genres', err.response.data.message);
                } else {
                    toastr.error('Choose genres', 'Something goes wrong');
                }
            });
    };
};

export const chooseGenres = (genresData, taskId, history) => {
    axios.post('/process/' + taskId, genresData)
        .then(() => {
            history.push('/sign-up-finished');
        }, (err) => {
            toastr.error('Choose genres', err.response.data.message);
        })
};