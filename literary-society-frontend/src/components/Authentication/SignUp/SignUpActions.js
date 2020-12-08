import { useHistory } from 'react-router';
import axios from '../../../axios-endpoint';

import * as actionTypes from './SignUpActionTypes';


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

        axios.get('/readers/start-registration')
            .then(response => {
                console.log(response.data)
                dispatch(fetchFormSuccess(response.data.formFields, response.data.processInstanceId, response.data.taskId));
            })
            .catch(err => {
                if (err.response) {
                    dispatch(fetchFormFail(err.response.data.error));
                }

            })
    };
};


export const signUpStart = () => {
    return {
        type: actionTypes.SIGN_UP_START
    };
};

export const signUpSuccess = () => {
    return {
        type: actionTypes.SIGN_UP_SUCCESS
    };
};

export const signUpFail = (error) => {
    return {
        type: actionTypes.SIGN_UP_FAIL,
        error: error
    };
};


export const signUp = (signUpData, taskId) => {
    return dispatch => {
        dispatch(signUpStart());
        // axios.post('/process/'.concat(taskId),signUpData)
        //     .then(() => {
        //         dispatch(signUpSuccess());
        //         const history = useHistory();
        //         history.push('/');
        //     })
        //     .catch(err => {
        //         console.log("ajk")
        //         // dispatch(signUpFail(err.response.data.error)); 
        //     })
        axios.post('/process/'.concat(taskId), signUpData)
            .then((response) => {
                dispatch(signUpSuccess());
                console.log("Success")
            }, (err) => {
                console.log("Failed")
                dispatch(signUpFail(err.response.data.error));
            })
    };
};