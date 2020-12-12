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

export const fetchForm = () => {
    return dispatch => {
        axios.get('/readers/beta/choose-genres')
            .then(response => {
                console.log(response.data)
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

export const chooseGenres = (genresData, taskId) => {
    axios.post('/process/' + taskId, genresData)
        .then(() => {
            toastr.success('Choose genres', 'Success');
        }, (err) => {
            toastr.error('Choose genres', err.response.data.message);
        })
};