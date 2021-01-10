import axios from '../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './PlagiarismComplaintActionTypes';

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

export const validateSuccess = () => {
    return {
        type: actionTypes.VALIDATE_SUCCESS,
    };
};


export const validateFail = () => {
    return {
        type: actionTypes.VALIDATE_FAIL,
    };
};

export const fetchForm = (piId, taskId) => {
    return dispatch => {
        axios.get(`/books/plagiarism-complaint-form?piId=${piId}&taskId=${taskId}`)
            .then(response => {
                dispatch(fetchFormSuccess(response.data.formFields, response.data.processInstanceId, response.data.taskId));
            })
            .catch(err => {
                if (err.response) {
                    dispatch(fetchFormFail(err.response.data.message));
                    toastr.error('Create Publication Request', err.response.data.message);
                } else {
                    toastr.error('Create Publication Request', 'Something went wrong');
                }
            });
    };
};


export const validateBook = (bookTitle, writerName) => {
    return dispatch => {
        axios.get(`/books/validate?bookTitle=${bookTitle}&writerName=${writerName}`)
            .then((response) => {
                if(response.data == true) {
                    dispatch(validateSuccess())
                }
                else {
                    dispatch(validateFail())
                }

            }).catch(err => {
               dispatch(validateFail())
            });
    }
};

export const clearValidation = () => {
        return {
            type: actionTypes.CLEAR_VALIDATION,
        }    
};

export const createRequest = (data, taskId, history) => {
        axios.post(`/process/${taskId}`, data)
            .then(() => {
                toastr.success('Plagiarism Complaint','Success');
                history.push('/tasks');
            }).catch(err => {
                toastr.error('Plagiarism Complaint', err.response.data.message);
                history.push('/tasks');
            });
};