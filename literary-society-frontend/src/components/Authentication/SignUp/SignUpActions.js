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
                dispatch(fetchFormSuccess(response.data.formFields, response.data.processInstanceId, response.data.taskId));
            })
            .catch(err => {
                if (err.response) {
                    dispatch(fetchFormFail(err.response.data.message));
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
        //         // dispatch(signUpFail(err.response.data.error)); 
        //     })
        axios.post('/process/'.concat(taskId), signUpData)
            .then(() => {
                dispatch(signUpSuccess());
            }, (err) => {
                dispatch(signUpFail(err.response.data.error));
            })
    };
};