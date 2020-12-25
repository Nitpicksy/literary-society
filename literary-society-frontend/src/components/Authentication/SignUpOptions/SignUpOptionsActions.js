import axios from '../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from '../SignUpOptions/SignUpOptionsActionTypes';

export const setSignUpType = (signUpType) => {
    return {
        type: actionTypes.SIGN_UP_TYPE,
        signUpType: signUpType
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
    return (dispatch, getState) => {

        const signUpType = getState().signUpOptions.signUpType;

        axios.get(`/${signUpType}/start-registration`)
            .then(response => {
                dispatch(startProcessSuccess(response.data.processInstanceId, response.data.taskId));
            })
            .catch(err => {
                if (err.response) {
                    dispatch(startProcessFail(err.response.data.message));
                    toastr.error('Sign up', err.response.data.message);
                } else {
                    toastr.error('Sign up', 'Something went wrong');
                }
            });
    };
};

export const clearProcessState = () => {
    return {
        type: actionTypes.CLEAR_PROCESS_STATE
    };
};
