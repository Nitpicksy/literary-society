import axios from '../../../axios-endpoint';
import {toastr} from 'react-redux-toastr';
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
                    dispatch(fetchFormFail(err.response.data.message));
                    toastr.error('Sign up',err.response.data.message);
                }else {
                    toastr.error('Sign up','Something goes wrong');
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
        axios.post('/process/' + taskId, signUpData)
            .then(() => {
                dispatch(signUpSuccess());
                toastr.success('Sign up', 'Success');
            }, (err) => {
                toastr.error('Sign up',err.response.data.message);
                dispatch(signUpFail(err.response.data.message));
            })
    };
};